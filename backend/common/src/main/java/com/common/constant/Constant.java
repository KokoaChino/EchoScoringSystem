package com.common.constant;

import java.nio.charset.StandardCharsets;


public class Constant {

    public final static String SECRET = "VT9c0zCizY7JtZB7fzmyKPe1DFWjPIPk"; // 生产环境要换
    public final static byte[] SECRET_BYTES = SECRET.getBytes(StandardCharsets.UTF_8);

    public final static String USER_CONTEXT_CACHE_KEY_PREFIX = "UserContext：";

    public final static String AUTH_SERVICE_DB = "auth_service_db";
    public final static String ECHO_SERVICE_DB = "echo_service_db";
    public final static String MESSAGE_SERVICE_DB = "message_service_db";
    public final static String PAY_SERVICE_DB = "pay_service_db";
}