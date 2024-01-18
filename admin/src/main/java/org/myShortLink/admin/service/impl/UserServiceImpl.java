package org.myShortLink.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.common.convention.error.BaseErrorCode;
import org.myShortLink.admin.common.convention.exception.ClientException;
import org.myShortLink.admin.dao.entity.User;
import org.myShortLink.admin.dao.repository.UserRepository;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRespDTO getUserByUsername(String username) {
        Optional<User> studentByUsername = userRepository.findByUsername(username);
        if (studentByUsername.isEmpty()) {
            throw new ClientException(BaseErrorCode.USER_NULL_ERROR);
        }
        log.debug("Fetched studentByUsername: {}", studentByUsername.get());
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(studentByUsername.get(), res);
        return res;
    }

    public Boolean hasUsernameRegistered(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
