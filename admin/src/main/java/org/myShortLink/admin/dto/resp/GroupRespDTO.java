package org.myShortLink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupRespDTO {
    private String gid;
    private String groupName;
    private String username;
    private Integer sortOrder;
}
