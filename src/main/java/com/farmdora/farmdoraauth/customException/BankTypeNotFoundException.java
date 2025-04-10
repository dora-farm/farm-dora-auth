package com.farmdora.farmdoraauth.customException;

import com.farmdora.farmdoraauth.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class BankTypeNotFoundException extends CustomException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "존재하지 않은 은행 타입입니다.";
    }
}
