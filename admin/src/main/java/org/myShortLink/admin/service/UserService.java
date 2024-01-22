package org.myShortLink.admin.service;

import org.myShortLink.admin.dto.req.UserLoginReqDTO;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.req.UserUpdateReqDTO;
import org.myShortLink.admin.dto.resp.UserLoginRespDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;

public interface UserService {

        UserRespDTO getUserByUsername(String username);

        Boolean hasUsernameRegistered(String username);

        Boolean hasEmailRegistered(String email);

        void register(UserRegisterReqDTO reqBody);

        void update(UserUpdateReqDTO reqBody);

        UserLoginRespDTO login(UserLoginReqDTO reqBody);
}
