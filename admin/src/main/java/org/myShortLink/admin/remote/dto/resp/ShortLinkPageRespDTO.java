package org.myShortLink.admin.remote.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortLinkPageRespDTO {
    private Long id;
    private String domain;
    private String shortUri;
    private String fullShortUrl;
    private String originalUrl;
    private String gid;
    private Integer validDateType;
    private LocalDateTime validDate;
    private LocalDateTime createdAt;
    private String description;
    private String favicon;
}
