package org.myShortLink.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.dto.req.UserLoginReqDTO;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.req.UserUpdateReqDTO;
import org.myShortLink.admin.dto.resp.UserLoginRespDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.myShortLink.common.convention.result.Result;
import org.myShortLink.common.convention.result.Results;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    @GetMapping("/user/checkUsernameExistence")
    public Result<Boolean> checkUsernameExistence(@RequestParam("username") String username) {
        return Results.success(userService.hasUsernameRegistered(username));
    }

    @GetMapping("/user/checkEmailExistence")
    public Result<Boolean> checkUserEmailExistence(@RequestParam("email") String email) {
        return Results.success(userService.hasEmailRegistered(email));
    }

    @GetMapping("/user/checkPhoneNumberExistence")
    public Result<Boolean> checkUserPhoneNumberExistence(@RequestParam("phoneNumber") String phoneNumber) {
        return Results.success(userService.hasPhoneNumberRegistered(phoneNumber));
    }

    @PostMapping("/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO reqBody) {
        userService.register(reqBody);
        return Results.success();
    }

    @PutMapping("/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO reqBody) {
        userService.update(reqBody);
        return Results.success();
    }

    @PostMapping("/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO reqBody) {
        UserLoginRespDTO res = userService.login(reqBody);
        log.debug("see user login token {}", res);
        return Results.success(res);
    }

    @GetMapping("/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("username") String username,
                                      @RequestParam("token") String token) {
        return Results.success(userService.checkLogin(username, token));
    }

    @DeleteMapping("/user/logout")
    public Result<Void> logout(@RequestParam("username") String username,
                               @RequestParam("token") String token) {
        userService.logout(username, token);
        return Results.success();
    }
}
