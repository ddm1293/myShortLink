package org.myShortLink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCountQueryRespDTO {
    private String gid;
    private Integer count;
}
