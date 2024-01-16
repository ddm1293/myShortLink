package org.myShortLink.admin.dto.resp;

import lombok.Data;

@Data
public class UserRespDTO {
    private Long id;
    private String username;
    private String email;
}
