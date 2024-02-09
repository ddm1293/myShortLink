package org.myShortLink.project.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.myShortLink.common.convention.exception.ServiceException;
import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.myShortLink.common.constant.ShortLinkConstant.DEFAULT_CACHE_VALID_TIME;

@Service
@RequiredArgsConstructor
public class LinkUtil {

    private final LinkRepository linkRepository;

    public static long getLinkCacheValidDate(LocalDateTime validDate) {
        return Optional.ofNullable(validDate)
                .map(date -> LocalDateTimeUtil.between(LocalDateTime.now(), date, ChronoUnit.MILLIS))
                .orElse(DEFAULT_CACHE_VALID_TIME);
    }

    public Link findLink(String gid, String fullShortUrl, Boolean archived, Boolean enabled) {
        return linkRepository.findLink(gid, fullShortUrl, archived, enabled).orElseThrow(() ->
                new ServiceException(MessageFormat.format(
                        "Cannot find corresponding link with Uri: {0} under group {1}", fullShortUrl, gid)));
    }

    public Optional<Link> findLinkOptional(String gid, String fullShortUrl, Boolean archived, Boolean enabled) {
        return linkRepository.findLink(gid, fullShortUrl, archived, enabled);
    }

    public Link findLink(String gid, String fullShortUrl) {
        return findLink(gid, fullShortUrl, false, true);
    }

    public static String ensureHttpPrefix(String url) {
        if (url == null) {
            return null;
        }
        return url.startsWith("http://") || url.startsWith("https://") ? url : "http://" + url;
    }
}
