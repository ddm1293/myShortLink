package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.common.convention.error.BaseErrorCode;
import org.myShortLink.admin.common.convention.exception.ClientException;
import org.myShortLink.admin.dao.entity.User;
import org.myShortLink.admin.dao.repository.UserRepository;
import org.myShortLink.admin.dto.req.UserLoginReqDTO;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.req.UserUpdateReqDTO;
import org.myShortLink.admin.dto.resp.UserLoginRespDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.myShortLink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RBloomFilter<String> usernameBloomFilter;

    private final RBloomFilter<String> userEmailBloomFilter;

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        User studentByUsername = fetchUserByUsername(username);
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(studentByUsername, res);
        return res;
    }

    @Override
    public Boolean hasUsernameRegistered(String username) {
        return usernameBloomFilter.contains(username);
    }

    @Override
    public Boolean hasEmailRegistered(String email) {
        return userEmailBloomFilter.contains(email);
    }

    @Override
    @Transactional
    public void register(UserRegisterReqDTO reqBody) {
        if (hasUsernameRegistered(reqBody.getUsername())) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }

        if (hasEmailRegistered(reqBody.getEmail())) {
            throw new ClientException(BaseErrorCode.USER_EMAIL_EXIST_ERROR);
        }

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + reqBody.getUsername());

        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    userRepository.save(BeanUtil.toBean(reqBody, User.class));
                    usernameBloomFilter.add(reqBody.getUsername());
                    userEmailBloomFilter.add(reqBody.getEmail());
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                throw new ClientException(BaseErrorCode.USER_REGISTRATION_BUSY);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ClientException(BaseErrorCode.USER_REGISTRATION_INTERRUPTED);
        } catch (Exception e) {
            log.error("Error Saving User:", e);
            throw new ClientException(BaseErrorCode.USER_SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void update(UserUpdateReqDTO reqBody) {
        User user = fetchUserByUsername(reqBody.getUsername());
        if (!hasEmailRegistered(reqBody.getEmail())) {
            user.setEmail(reqBody.getEmail());
            user.setPassword(reqBody.getPassword());
            userRepository.save(user);
        } else {
            throw new ClientException(BaseErrorCode.USER_EMAIL_EXIST_ERROR);
        }
    }

    private User fetchUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ClientException(BaseErrorCode.USER_NULL_ERROR));
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO reqBody) {
        return null;
    }
}
