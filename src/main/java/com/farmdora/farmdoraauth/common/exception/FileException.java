package com.farmdora.farmdoraauth.common.exception;

import org.springframework.http.HttpStatus;

public class FileException extends BaseException{

    public FileException(String message, HttpStatus status) {
        super(message, status);
    }
}
