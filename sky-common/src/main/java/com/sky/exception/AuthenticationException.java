package com.sky.exception;

/**
 * 账号被锁定异常
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException() {
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

}
