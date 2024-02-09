package org.myShortLink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.myShortLink.project.dto.req.RecycleBinSaveReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkPageRespDTO;
import org.myShortLink.project.service.RecycleBinService;
import org.myShortLink.project.utils.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.myShortLink.common.constant.RedisCacheConstant.ROUTE_TO_SHORT_LINK_KEY;
import static org.myShortLink.project.utils.LinkUtil.ensureHttpPrefix;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final LinkRepository linkRepository;

    private final LinkUtil linkUtil;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public void saveRecycleBin(RecycleBinSaveReqDTO reqBody) {
        Link link = linkUtil.findLink(reqBody.getGid(), reqBody.getFullShortUrl());
        linkRepository.disableLink(link.getId(), link.getGid());
        stringRedisTemplate.delete(
                String.format(ROUTE_TO_SHORT_LINK_KEY, reqBody.getFullShortUrl())
        );
    }

    @Override
    public Page<ShortLinkPageRespDTO> getDisabledShortLinksIntoPage(String gid, String orderTag, int currentPage, int size) {
        Pageable pageable = PageRequest.of(currentPage, size);
        return linkRepository.findDisabledLinksUnderSameGroup(gid, pageable)
                .map(link -> {
                    ShortLinkPageRespDTO resp = BeanUtil.toBean(link, ShortLinkPageRespDTO.class);
                    resp.setDomain(ensureHttpPrefix(resp.getDomain()));
                    return resp;
                });
    }
}
