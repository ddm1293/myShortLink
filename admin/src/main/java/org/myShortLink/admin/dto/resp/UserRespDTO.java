package org.myShortLink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRespDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
}
