package org.myShortLink.admin.service;

import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;

public interface UserService {

        UserRespDTO getUserByUsername(String username);

        Boolean hasUsernameRegistered(String username);

        void register(UserRegisterReqDTO reqParam);
}