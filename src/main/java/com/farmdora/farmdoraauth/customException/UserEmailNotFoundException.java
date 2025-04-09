package com.farmdora.farmdoraauth.customException;

import com.farmdora.farmdoraauth.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserEmailNotFoundException extends CustomException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "사용가능한 이메일 입니다.";
    }
}
