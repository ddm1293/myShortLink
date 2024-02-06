package org.myShortLink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.common.enums.ValidDateTypeEnum;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.entity.LinkRouter;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.myShortLink.project.dao.repository.LinkRouterRepository;
import org.myShortLink.project.dao.repository.projection.GroupLinkCount;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateLinkGroupReqDTO;
import org.myShortLink.project.dto.req.ShortLinkUpdateReqDTO;
import org.myShortLink.project.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.LinkService;
import org.myShortLink.project.utils.MurmurHashUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.myShortLink.common.constant.RedisCacheConstant.*;
import static org.myShortLink.project.common.constant.LinkGenerateConstant.SUFFIX_GENERATE_CAP;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final LinkRouterRepository linkRouterRepository;

    private final RBloomFilter<String> shortUrlCreateBloomFilter;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedissonClient redissonClient;

    @Override
    @Transactional
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody) {
        String suffix = generateSuffix(reqBody.getOriginalUrl(), reqBody.getDomain());
        String fullShortUrl = reqBody.getDomain() + "/" + suffix;
        Link link = Link.builder()
                .gid(reqBody.getGid())
                .domain(reqBody.getDomain())
                .shortUri(suffix)
                .fullShortUrl(fullShortUrl)
                .originalUrl(reqBody.getOriginalUrl())
                .enabled(true)
                .createdType(reqBody.getCreatedType())
                .validDateType(reqBody.getValidDateType())
                .validDate(reqBody.getValidDate())
                .description(reqBody.getDescription())
                .build();
        checkValidDateType(link);

        LinkRouter linkRouter = LinkRouter.builder()
                .gid(reqBody.getGid())
                .fullShortUrl(fullShortUrl)
                .build();

        try {
            linkRepository.save(link);
            linkRouterRepository.save(linkRouter);
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate Full URL Found: ", e);
            shortUrlCreateBloomFilter.add(fullShortUrl);
            // TODO add new error code here
            throw new ServiceException("Duplicate Full URL Found");
        }

        shortUrlCreateBloomFilter.add(fullShortUrl);

        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl("http://" + link.getFullShortUrl())
                .originalUrl(reqBody.getOriginalUrl())
                .gid(reqBody.getGid())
                .build();
    }

    private void checkValidDateType(Link link) {
        if (Objects.equals(link.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType())) {
            link.setValidDate(null);
        }
    }

    private String generateSuffix(String originalURL, String domain) {
        int generateCount = 0;
        String suffix;
        while (true) {
            if (generateCount > SUFFIX_GENERATE_CAP) {
                // TODO add new error code here
                throw new ServiceException("Too many times generate suffix");
            }
            originalURL = generateCount == 0 ? originalURL : originalURL + System.currentTimeMillis();
            suffix = MurmurHashUtil.convertWithGuava(originalURL);
            if (!shortUrlCreateBloomFilter.contains(domain + "/" + suffix)) {
                break;
            }
            generateCount++;
        }
        return suffix;
    }

    @Override
    public Page<ShortLinkPageRespDTO> getShortLinksIntoPage(String gid, String orderTag, int currentPage, int size) {
        Pageable pageable = PageRequest.of(currentPage, size);
        return linkRepository.findLinksUnderSameGroup(gid, pageable)
                .map(link -> {
                    ShortLinkPageRespDTO resp = BeanUtil.toBean(link, ShortLinkPageRespDTO.class);
                    resp.setDomain(ensureHttpPrefix(resp.getDomain()));
                    return resp;
                });
    }

    private String ensureHttpPrefix(String url) {
        if (url == null) {
            return null;
        }
        return url.startsWith("http://") || url.startsWith("https://") ? url : "http://" + url;
    }

    @Override
    public List<GroupCountQueryRespDTO> groupCount(List<String> gidList) {
        List<GroupCountQueryRespDTO> res = new ArrayList<>();
        gidList.forEach(gid -> {
            GroupLinkCount groupLinkCount = linkRepository.countGroupLink(gid);
            if (groupLinkCount != null) {
                res.add(new GroupCountQueryRespDTO(groupLinkCount.getGid(), groupLinkCount.getCount()));
            }
        });
        return res;
    }

    @Override
    @Transactional
    public void updateLink(ShortLinkUpdateReqDTO reqBody) {
        Link link = findLink(reqBody.getGid(), reqBody.getFullShortUrl());
        link.setOriginalUrl(reqBody.getOriginalUrl());
        link.setValidDateType(reqBody.getValidDateType());
        link.setValidDate(reqBody.getValidDate());
        link.setDescription(reqBody.getDescription());
        checkValidDateType(link);
        linkRepository.save(link);
    }

    @Override
    @Transactional
    public void updateLinkGroup(ShortLinkUpdateLinkGroupReqDTO reqBody) {
        // TODO: should return an optional
        try {
            Link toBeDeleted = findLink(reqBody.getGid(), reqBody.getFullShortUrl());
            Link link = BeanUtil.copyProperties(toBeDeleted, Link.class);
            link.setId(null);
            link.setGid(reqBody.getGidChangedTo());
            linkRepository.deleteLink(reqBody.getGid(), reqBody.getFullShortUrl());
            linkRepository.save(link);
        } catch (Exception e) {
            log.error("Error deleting link when updating link group", e);
            // TODO: new Error code
            throw new ServiceException("Error deleting link when updating link group");
        }
    }


    @Override
    public void restoreUrl(String shortUri, ServletRequest req, ServletResponse resp) {
        String serverName = req.getServerName();
        String fullShortUrl = serverName + "/" + shortUri;
        String originalLink = stringRedisTemplate.opsForValue().get(String.format(ROUTE_TO_SHORT_LINK_KEY, fullShortUrl));

        // check cache first for the original link
        if (StringUtil.isNotBlank(originalLink)) {
            redirectTo((HttpServletResponse) resp, originalLink);
            return;
        }

        if (!shortUrlCreateBloomFilter.contains(fullShortUrl)) {
            return;
        }

        String routerIsNull = stringRedisTemplate.opsForValue().get(String.format(ROUTE_TO_SHORT_LINK_IS_NULL_KEY, fullShortUrl));
        if (StringUtil.isNotBlank(routerIsNull)) {
            return;
        }

        RLock lock = redissonClient.getLock(String.format(LOCK_SHORT_LINK_ROUTE_KEY, fullShortUrl));
        lock.lock();
        try {
            // double-checking lock
            originalLink = stringRedisTemplate.opsForValue().get(String.format(ROUTE_TO_SHORT_LINK_KEY, fullShortUrl));

            if (StringUtil.isNotBlank(originalLink)) {
                redirectTo((HttpServletResponse) resp, originalLink);
                return;
            }

            // non-existent in cache, get the original link in DB and store it in cache
            Optional<LinkRouter> linkRouter = linkRouterRepository.getLinkRouterFromFullShortUrl(fullShortUrl);
            if (linkRouter.isEmpty()) {
                stringRedisTemplate.opsForValue().set(String.format(ROUTE_TO_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                return;
            }

            Link link = findLink(linkRouter.get().getGid(), fullShortUrl);
            stringRedisTemplate.opsForValue().set(String.format(ROUTE_TO_SHORT_LINK_KEY, fullShortUrl), link.getOriginalUrl());
            redirectTo((HttpServletResponse) resp, link.getOriginalUrl());
        } finally {
            lock.unlock();
        }



    }

    private void redirectTo(HttpServletResponse resp, String originalLink) {
        try {
            resp.sendRedirect(originalLink);
        } catch (IOException e) {
            log.debug("see IOException", e);
            throw new ServiceException("Error when redirecting to original link");
        }
    }

    private Link findLink(String gid, String fullShortUrl) {
        return linkRepository.findLink(gid, fullShortUrl).orElseThrow(() ->
                new ServiceException(MessageFormat.format(
                        "Cannot find corresponding link with Uri: {0} under group {1}", fullShortUrl, gid)));
    }
}
