package com.yolo.mrs.utils;

public class RedisConstant {
    public static final String USER_LOGIN_KEY = "user:login:token:";
    //token过期时间，30分钟
    public static final int USER_LOGIN_TTL = 30;
    public static final String USER_LOGIN_CODE = "user:login:code:";
    //验证码过期时间，5分钟
    public static final int USER_LOGIN_CODE_TTL = 5;
}
