package com.farmdora.farmdoraauth.auth.register.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.auth.register.dto.StandardRegisterDto;
import com.farmdora.farmdoraauth.auth.register.responseMessage.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.StandardRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/standard/register")
public class StandardRegisterRestController {
    private final StandardRegisterService standardRegisterService;
    String EMAIL_SUB = "이메일 인증";
    private static final String EMAIL_TITLE = "이메일 인증 요청";
    private static final String EMAIL_CONTENT = "안녕하세요! 아래의 인증 코드를 일력하여 인증을 완료하세요:";

    @GetMapping("/idcheck")
    public HttpResponse idCheck(@RequestParam("id") String id) {

        standardRegisterService.idCheck(id);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.ID_CHECK_SUCCESS.getMessage())
                .data(null)
                .build();
    }

    @GetMapping("/emailcheck")
    public HttpResponse emailCheck(@RequestParam("email") String email) {
        standardRegisterService.emailCheck(email);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.EMAIL_CHECK_SUCCESS.getMessage())
                .data(null)
                .build();
    }

    @PostMapping("/send/email")
    public HttpResponse sendEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        log.info(email);
        standardRegisterService.sendVerificationEmail(email, EMAIL_SUB, EMAIL_TITLE, EMAIL_CONTENT);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.EMAIL_SEND_SUCCESS.getMessage())
                .data(true)
                .build();
    }

    @PostMapping("/verify/email")
    public HttpResponse verifyEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        String code = emailBody.get("code");

        if(standardRegisterService.verifyEmail(email, code)){
            return HttpResponse.builder()
                    .status(200)
                    .message(StandardRegisterMassage.EMAIL_VERIFY_SUCCESS.getMessage())
                    .data(true)
                    .build();
        }
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.EMAIL_VERIFY_FAIL.getMessage())
                .data(false)
                .build();
    }

    @PostMapping("/user")
    public HttpResponse registerUser(@ModelAttribute StandardRegisterDto standardRegisterDto) {

        log.info("요청 파라미터 {}", standardRegisterDto);

        boolean result = standardRegisterService.registerUser(standardRegisterDto);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.USER_REGISTER_SUCCESS.getMessage())
                .data(result)
                .build();
    }

}


