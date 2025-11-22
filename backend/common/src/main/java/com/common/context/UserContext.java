package com.common.context;

import com.common.entity.User;


public class UserContext {

    private final static ThreadLocal<User> CONTEXT = new ThreadLocal<>();

    public static void set(User user) {
        CONTEXT.set(user);
    }

    public static User get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static String getUsername() {
        User user = get();
        return user != null ? user.getUsername() : null;
    }
}