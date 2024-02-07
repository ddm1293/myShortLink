package org.myShortLink.common.constant;

public class RedisCacheConstant {

    public static final String LOCK_USER_REGISTER_KEY = "myShortLink_lock_user_register: ";

    public static final String ROUTE_TO_SHORT_LINK_KEY = "short_link_goto_%s";

    public static final String ROUTE_TO_SHORT_LINK_IS_NULL_KEY = "short_link_goto_is_null_%s";

    public static final String LOCK_SHORT_LINK_ROUTE_KEY = "short_link_lock_goto_%s";

    public static final String LINK_INFO_TITLE_KEY = "short_link_title: %s";
}
