package org.myShortLink.project.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortLinkUpdateReqDTO {
    private String gid;
    private String originalUrl;
    private String fullShortUrl;
    private Boolean validDateType;
    private LocalDateTime validDate;
    private String description;
}
