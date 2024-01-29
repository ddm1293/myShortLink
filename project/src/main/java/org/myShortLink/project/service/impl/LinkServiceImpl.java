package org.myShortLink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.myShortLink.project.dto.req.ShortLinkCreateReqDTO;
import org.myShortLink.project.dto.resp.ShortLinkCreateRespDTO;
import org.myShortLink.project.service.LinkService;
import org.myShortLink.project.utils.MurmurHashUtil;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqBody) {
        Link link = BeanUtil.toBean(reqBody, Link.class);
        String suffix = generateSuffix(reqBody.getOriginalURL());
        link.setFullShortURL(reqBody.getDomain() + "/" + suffix);
        linkRepository.save(link);
        return ShortLinkCreateRespDTO.builder()
                .fullShortURL(link.getFullShortURL())
                .originalUrl(reqBody.getOriginalURL())
                .gid(reqBody.getGid())
                .build();
    }

    private String generateSuffix(String originalURL) {
        return MurmurHashUtil.convertWithGuava(originalURL);
    }
}
