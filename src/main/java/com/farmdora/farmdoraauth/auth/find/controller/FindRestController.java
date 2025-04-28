package com.farmdora.farmdoraauth.auth.find.controller;

import com.farmdora.farmdoraauth.auth.find.dto.FindDto;
import com.farmdora.farmdoraauth.auth.find.dto.SendDto;
import com.farmdora.farmdoraauth.auth.find.service.FindService;
import com.farmdora.farmdoraauth.auth.register.message.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.UserRegisterService;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/find")
public class FindRestController {

    private final FindService findService;
    private final UserRegisterService userRegisterService;
    private static final String EMAIL_SUB = "이메일 인증";
    private static final String EMAIL_TITLE = "이메일 인증 요청";
    private static final String EMAIL_CONTENT = "안녕하세요! 아래의 인증 코드를 일력하여 인증을 완료하세요:";

    @PostMapping("/send/code")
    public HttpResponse sendVerificationCode(@RequestBody SendDto request) {
        String email = request.getEmail();
        boolean isEmail = findService.existEmail(email, request.getId(), request.getName());
        if (isEmail) {
            userRegisterService.sendVerificationEmail(email, EMAIL_SUB, EMAIL_TITLE, EMAIL_CONTENT);
            return HttpResponse.builder()
                    .status(200)
                    .message(StandardRegisterMassage.EMAIL_SEND_SUCCESS.getMessage())
                    .data(true)
                    .build();
        }else {
            throw new ResourceNotFoundException("아이디, 비번 찾기",email);
        }
    }

    @PostMapping("/send/value")
    public HttpResponse sendValue(@RequestBody FindDto requestBody) {
    log.info("이메일 인증 후 이메일로 찾은 값 보내기 {}", requestBody.getEmail());

        if (findService.sendFind(requestBody.getEmail(), requestBody.getCode(), requestBody.getFind())){
            return HttpResponse.builder()
                    .status(200)
                    .message(StandardRegisterMassage.EMAIL_VERIFY_SUCCESS.getMessage())
                    .data(true)
                    .build();
        }else {
            return HttpResponse.builder()
                    .status(500)
                    .message(StandardRegisterMassage.EMAIL_VERIFY_FAIL.getMessage())
                    .data(false)
                    .build();
        }
    }
}
