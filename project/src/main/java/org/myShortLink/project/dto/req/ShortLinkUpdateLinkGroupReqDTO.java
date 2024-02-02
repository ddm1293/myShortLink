package org.myShortLink.project.dto.req;

import lombok.Data;

@Data
public class ShortLinkUpdateLinkGroupReqDTO {
    private String gid;
    private String fullShortUrl;
}
