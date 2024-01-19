package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.common.convention.error.BaseErrorCode;
import org.myShortLink.admin.common.convention.exception.ClientException;
import org.myShortLink.admin.dao.entity.User;
import org.myShortLink.admin.dao.repository.UserRepository;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    private final UserRepository userRepository;

    public UserRespDTO getUserByUsername(String username) {
        Optional<User> studentByUsername = userRepository.findByUsername(username);
        if (studentByUsername.isEmpty()) {
            throw new ClientException(BaseErrorCode.USER_NULL_ERROR);
        }
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(studentByUsername.get(), res);
        return res;
    }

    public Boolean hasUsernameRegistered(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    public void register(UserRegisterReqDTO reqParam) {
        if (hasUsernameRegistered(reqParam.getUsername())) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }
        try {
            userRepository.save(BeanUtil.toBean(reqParam, User.class));
        } catch (Exception e) {
            log.error("Error Saving User:", e);
            throw new ClientException(BaseErrorCode.USER_SAVE_ERROR);
        }
    }
}