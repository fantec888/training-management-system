package com.training.management.common;

import com.training.management.domain.entity.SysUser;

public final class RequestContext {

    private static final ThreadLocal<SysUser> CURRENT_USER = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void setCurrentUser(SysUser user) {
        CURRENT_USER.set(user);
    }

    public static SysUser getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
