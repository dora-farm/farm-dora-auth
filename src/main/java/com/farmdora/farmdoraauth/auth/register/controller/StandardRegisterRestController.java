package com.farmdora.farmdoraauth.auth.register.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.auth.register.dto.StandardRegisterDto;
import com.farmdora.farmdoraauth.auth.register.responseMessage.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.StandardRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/standard/register")
public class StandardRegisterRestController {
    private final StandardRegisterService standardRegisterService;

    @GetMapping("/idcheck")
    public ResponseEntity<?> idCheck(@RequestParam("id") String id) {

        standardRegisterService.idCheck(id);
        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .status(200)
                        .message(StandardRegisterMassage.ID_CHECK_SUCCESS.getMessage())
                        .data(null)
                        .build());
    }

    @GetMapping("/emailcheck")
    @Transactional
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
        standardRegisterService.sendVerificationEmail(email);
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


