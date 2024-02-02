package org.myShortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myShortLink.admin.dao.entity.User;
import org.myShortLink.admin.dao.repository.UserRepository;
import org.myShortLink.admin.dto.req.UserLoginReqDTO;
import org.myShortLink.admin.dto.req.UserRegisterReqDTO;
import org.myShortLink.admin.dto.req.UserUpdateReqDTO;
import org.myShortLink.admin.dto.resp.UserLoginRespDTO;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.GroupService;
import org.myShortLink.admin.service.UserService;
import org.myShortLink.common.convention.error.BaseErrorCode;
import org.myShortLink.common.convention.exception.ClientException;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.myShortLink.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RBloomFilter<String> usernameBloomFilter;

    private final RBloomFilter<String> userEmailBloomFilter;

    private final RBloomFilter<String> userPhoneNumberBloomFilter;

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;

    private final BCryptPasswordEncoder passwordEncoder;

    private final StringRedisTemplate stringRedisTemplate;

    private final GroupService groupService;

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
    public Boolean hasPhoneNumberRegistered(String phoneNumber) {
        return userPhoneNumberBloomFilter.contains(phoneNumber);
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

        if (hasPhoneNumberRegistered(reqBody.getPhoneNumber())) {
            throw new ClientException(BaseErrorCode.USER_PHONE_NUMBER_EXIST_ERROR);
        }

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + reqBody.getUsername());

        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    String encodedPassword = passwordEncoder.encode(reqBody.getPassword());
                    reqBody.setPassword(encodedPassword);
                    userRepository.save(BeanUtil.toBean(reqBody, User.class));
                    usernameBloomFilter.add(reqBody.getUsername());
                    userEmailBloomFilter.add(reqBody.getEmail());
                    userPhoneNumberBloomFilter.add(reqBody.getPhoneNumber());

                    // create a default group during new user registration
                    // TODO set "DEFAULT" here to be a constant
                    groupService.addGroup("DEFAULT", reqBody.getUsername());
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
        // TODO check user is logged in to proceed updating
        User user = fetchUserByUsername(reqBody.getUsername());
        user.setEmail(reqBody.getEmail());
        user.setPassword(reqBody.getPassword());
        user.setPhoneNumber(reqBody.getPhoneNumber());
        userRepository.save(user);
    }

    private User fetchUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ClientException(BaseErrorCode.USER_NULL_ERROR));
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO reqBody) {
        User matchedUser = fetchUserByUsername(reqBody.getUsername());

        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("login_" + reqBody.getUsername()))) {
            throw new ClientException(BaseErrorCode.USER_ALREADY_LOGGED_IN_ERROR);
        }

        if (!passwordEncoder.matches(reqBody.getPassword(), matchedUser.getPassword())) {
            throw new ClientException(BaseErrorCode.PASSWORD_MISMATCH_ERROR);
        }

        String uuid = UUID.randomUUID().toString();
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            String userJson = om.writeValueAsString(matchedUser);
            stringRedisTemplate.opsForHash().put("login_" + reqBody.getUsername(), uuid, userJson);
            stringRedisTemplate.expire("login_" + reqBody.getUsername(), 30L, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            log.error("Error Parsing User:", e);
            throw new ClientException(BaseErrorCode.USER_JSON_PARSE_ERROR);
        }
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_" + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        if (!checkLogin(username, token)) {
            throw new ClientException(BaseErrorCode.USER_NOT_LOGGED_IN_ERROR);
        }
        stringRedisTemplate.delete("login_" + username);
    }
}
