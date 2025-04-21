package com.farmdora.farmdoraauth.common.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException {

    public AuthenticationException() {
        super("로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED);
    }
}
