package org.myShortLink.admin.dto.req;

import lombok.Data;

@Data
public class GroupSortReqDTO {
    private String gid;
    private Integer sortOrder;
}
