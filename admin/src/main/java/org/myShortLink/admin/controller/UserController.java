package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.common.convention.Result;
import org.myShortLink.admin.common.convention.error.BaseErrorCode;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO res = userService.getUserByUserName(username);
        if (res == null) {
            return new Result<UserRespDTO>().setCode(BaseErrorCode.USER_NULL_ERROR.code())
                    .setMessage(BaseErrorCode.USER_NULL_ERROR.message());
        } else {
            return new Result<UserRespDTO>().setCode("0").setData(userService.getUserByUserName(username));
        }
    }
}
