package org.myShortLink.admin.dto.req;

import lombok.Data;

@Data
public class UserRegisterReqDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
