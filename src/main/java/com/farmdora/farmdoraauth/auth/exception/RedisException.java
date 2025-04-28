package com.farmdora.farmdoraauth.auth.exception;

import com.farmdora.farmdoraauth.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class RedisException extends BaseException {
    public RedisException(String message, HttpStatus status) {
        super(message, status);
    }
}
