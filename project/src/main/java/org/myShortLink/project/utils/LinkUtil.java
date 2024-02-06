package org.myShortLink.project.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.myShortLink.common.constant.ShortLinkConstant.DEFAULT_CACHE_VALID_TIME;

public class LinkUtil {

    public static long getLinkCacheValidDate(LocalDateTime validDate) {
        return Optional.ofNullable(validDate)
                .map(date -> LocalDateTimeUtil.between(LocalDateTime.now(), date, ChronoUnit.MILLIS))
                .orElse(DEFAULT_CACHE_VALID_TIME);
    }
}
