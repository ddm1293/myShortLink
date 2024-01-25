package org.myShortLink.admin.common.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String username = httpRequest.getHeader("username");
        String token = httpRequest.getHeader("token");
        log.debug("see filter got triggered: {}, {}", username, token);
        if (username != null && token != null) {
            Object userInfoJson = stringRedisTemplate.opsForHash().get("login_" + username, token);
            if (userInfoJson != null) {
                UserInfo userInfo = new ObjectMapper().readValue(userInfoJson.toString(), UserInfo.class);
                log.debug("see userInfo: {}", userInfo);
                UserContext.setUser(userInfo);
            }
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
