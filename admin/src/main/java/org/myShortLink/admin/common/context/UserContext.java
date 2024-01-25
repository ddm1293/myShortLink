package org.myShortLink.admin.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

public final class UserContext {

    private static final ThreadLocal<UserInfo> USER_INFO_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setUser(UserInfo user) {
        USER_INFO_THREAD_LOCAL.set(user);
    }

    public static String getUserId() {
        UserInfo user = USER_INFO_THREAD_LOCAL.get();
        return Optional.ofNullable(user).map(UserInfo::getUserId).orElse(null);
    }

    public static String getUsername() {
        UserInfo user = USER_INFO_THREAD_LOCAL.get();
        return Optional.ofNullable(user).map(UserInfo::getUsername).orElse(null);
    }

    public static String getToken() {
        UserInfo user = USER_INFO_THREAD_LOCAL.get();
        return Optional.ofNullable(user).map(UserInfo::getToken).orElse(null);
    }

    public static void removeUser() {
        USER_INFO_THREAD_LOCAL.remove();
    }
}
