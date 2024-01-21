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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static org.myShortLink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;

    public UserRespDTO getUserByUsername(String username) {
        User studentByUsername = userRepository.findByUsername(username)
                .orElseThrow(() -> new ClientException(BaseErrorCode.USER_NULL_ERROR));
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(studentByUsername, res);
        return res;
    }

    public Boolean hasUsernameRegistered(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    public void register(UserRegisterReqDTO reqParam) {
        if (hasUsernameRegistered(reqParam.getUsername())) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + reqParam.getUsername());

        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    userRepository.save(BeanUtil.toBean(reqParam, User.class));
                    userRegisterCachePenetrationBloomFilter.add(reqParam.getUsername());
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

}
