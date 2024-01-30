package org.myShortLink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkPageRespDTO {
    private Long id;
    private String domain;
    private String shortUri;
    private String fullShortUrl;
    private String originalUrl;
    private String gid;
    private Integer validDateType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;
    private String description;
    private String favicon;
}
