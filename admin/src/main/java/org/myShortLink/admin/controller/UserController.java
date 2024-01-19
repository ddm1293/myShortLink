package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.myShortLink.admin.common.convention.result.Result;
import org.myShortLink.admin.common.convention.result.Results;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    @GetMapping("/user/checkExistence")
    public Result<Boolean> checkUsernameExistence(@RequestParam("username") String username) {
        return Results.success(userService.hasUsernameRegistered(username));
    }

    @PostMapping("/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO reqBody) {
        userService.register(reqBody);
        return Results.success();
    }
}