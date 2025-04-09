package com.farmdora.farmdoraauth.customException;

import com.farmdora.farmdoraauth.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserIdNotFoundException extends CustomException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "사용 가능한 아이디 입니다.";
    }
}
