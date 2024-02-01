package org.myShortLink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.myShortLink.project.dao.repository.projection.GroupLinkCount;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.GroupCountQueryRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.LinkService;
import org.myShortLink.project.utils.MurmurHashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.myShortLink.project.common.constant.LinkGenerateConstant.SUFFIX_GENERATE_CAP;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final RBloomFilter<String> shortUrlCreateBloomFilter;

    @Override
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

        try {
            linkRepository.save(link);
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate Full URL Found: ", e);
            shortUrlCreateBloomFilter.add(fullShortUrl);
            // TODO add new error code here
            throw new ServiceException("Duplicate Full URL Found");
        }

        shortUrlCreateBloomFilter.add(fullShortUrl);

        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(link.getFullShortUrl())
                .originalUrl(reqBody.getOriginalUrl())
                .gid(reqBody.getGid())
                .build();
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
    public Page<ShortLinkPageRespDTO> getShortLinks(String gid, String orderTag, int currentPage, int size) {
        Pageable pageable = PageRequest.of(currentPage, size);
        return linkRepository.findLinks(gid, pageable)
                .map(link -> BeanUtil.toBean(link, ShortLinkPageRespDTO.class));
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
}
