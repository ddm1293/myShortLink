package org.myShortLink.project.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.dto.resp.OriginalLinkInfoRespDTO;
import org.myShortLink.project.service.LinkInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.myShortLink.common.constant.RedisCacheConstant.LINK_INFO_KEY;
import static org.myShortLink.common.constant.ShortLinkConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public OriginalLinkInfoRespDTO getOriginalLinkInfo(String link) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String linkInfoJson = stringRedisTemplate.opsForValue().get(String.format(LINK_INFO_KEY, link));

            if (StrUtil.isNotBlank(linkInfoJson)) {
                OriginalLinkInfoRespDTO decoded = mapper.readValue(linkInfoJson, OriginalLinkInfoRespDTO.class);
                if (!StrUtil.equals(decoded.getTitle(), DEFAULT_TITLE)
                        && !StrUtil.equals(decoded.getFavicon(), DEFAULT_FAVICON)) {
                    return decoded;
                }
            }

            Connection.Response resp = Jsoup.connect(link).execute();

            if (resp.statusCode() == HttpStatus.HTTP_OK) {
                Document doc = resp.parse();
                String title = StrUtil.isNotBlank(doc.title()) ? doc.title() : DEFAULT_TITLE;
                String favicon = getFavicon(doc);

                OriginalLinkInfoRespDTO res = OriginalLinkInfoRespDTO.builder()
                        .title(title)
                        .favicon(favicon)
                        .build();

                stringRedisTemplate.opsForValue().set(String.format(LINK_INFO_KEY, link), mapper.writeValueAsString(res));
                return res;
            } else {
                throw new ServiceException("Received non-OK status fetching website information.");
            }
        } catch (Exception e) {
            log.debug("Error occurs fetching website information", e);
            throw new ServiceException(String.format("Error occurs fetching website information: %s", link));
        }
    }

    private String getFavicon(Document doc) {
        String[] relValues = {
                "shortcut icon",
                "icon",
                "apple-touch-icon",
                "apple-touch-icon-precomposed"
        };

        for (String rel : relValues) {
            Elements elements = doc.head().select(String.format("link[rel=%s]", rel));

            // Special handling for multiple "icon" elements
            if (StrUtil.equals(rel, "icon") && elements.size() > 1) {
                for (Element element : elements) {
                    String sizes = element.attr("sizes");
                    if (StrUtil.isNotBlank(sizes)
                            && StrUtil.contains(sizes, SHORT_LINK_FAVICON_SIZE)
                            && StrUtil.isNotBlank(element.absUrl("href"))) {
                        return element.absUrl("href");
                    }
                }
            }

            // Return the first found favicon if not specifically looking for a size
            Element element = elements.first();
            if (element != null && StrUtil.isNotBlank(element.absUrl("href"))) {
                return element.absUrl("href");
            }
        }

        // Return a default favicon if none is found
        return DEFAULT_FAVICON;
    }
}
