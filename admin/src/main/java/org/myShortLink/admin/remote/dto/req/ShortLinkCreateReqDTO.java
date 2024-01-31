package org.myShortLink.admin.remote.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkCreateReqDTO {
    private String gid;
    private String domain;
    private String originalUrl;
    private Boolean createdType;
    private Boolean validDateType;
    private LocalDateTime validDate;
    private String description;
}
