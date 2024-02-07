package org.myShortLink.project.service.impl;

import cn.hutool.http.HttpStatus;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.dto.resp.OriginalLinkInfoRespDTO;
import org.myShortLink.project.service.LinkInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.myShortLink.common.constant.RedisCacheConstant.LINK_INFO_TITLE_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public OriginalLinkInfoRespDTO getOriginalLinkInfo(String link) {
        String title = getLinkInfo(link);
        return OriginalLinkInfoRespDTO.builder()
                .title(StringUtil.isNotBlank(title) ? title : "Title Not Available")
                .build();
    }

    private String getLinkInfo(String link) {
        String title = stringRedisTemplate.opsForValue().get(String.format(LINK_INFO_TITLE_KEY, link));

        if (StringUtil.isNotBlank(title)) {
            return title;
        }

        try {
            Connection.Response resp = Jsoup.connect(link).execute();

            if (resp.statusCode() == HttpStatus.HTTP_OK) {
                Document doc = Jsoup.connect(link).get();
                title = doc.title();

                if (StringUtil.isNotBlank(title)) {
                    stringRedisTemplate.opsForValue().set(String.format(LINK_INFO_TITLE_KEY, link), title);
                    return title;
                }

                return null;
            } else {
                throw new ServiceException("Received non-OK status fetching website information.");
            }
        } catch (Exception e) {
            log.debug("Error occurs fetching website information", e);
            throw new ServiceException(String.format("Error occurs fetching website information: %s", link));
        }
    }
}
