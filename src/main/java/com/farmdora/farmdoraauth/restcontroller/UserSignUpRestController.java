package com.farmdora.farmdoraauth.restcontroller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.dto.UserSignUpDto;
import com.farmdora.farmdoraauth.responseMessage.UserRegisterMassage;
import com.farmdora.farmdoraauth.service.UserSignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserSignUpRestController {
    private final UserSignUpService userSignUpService;

    @GetMapping("/idcheck")
    public ResponseEntity<?> idCheck(@RequestParam("id") String id) {
        userSignUpService.idCheck(id);
        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .status(200)
                        .message(UserRegisterMassage.ID_CHECK_SUCCESS.getMessage())
                        .data(null)
                        .build());
    }

    @GetMapping("/emailcheck")
    @Transactional
    public HttpResponse emailCheck(@RequestParam("email") String email) {
        userSignUpService.emailCheck(email);
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.EMAIL_CHECK_SUCCESS.getMessage())
                .data(null)
                .build();
    }

    @PostMapping("/send/email")
    public HttpResponse sendEmail(@RequestParam("email") String email) {
        userSignUpService.sendVerificationEmail(email);
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.EMAIL_SEND_SUCCESS.getMessage())
                .data(true)
                .build();
    }

    @PostMapping("/verify/email")
    public HttpResponse verifyEmail(@RequestParam("email") String email,
                                    @RequestParam("code") String code) {
        if(userSignUpService.verifyEmail(email, code)){
            return HttpResponse.builder()
                    .status(200)
                    .message(UserRegisterMassage.EMAIL_VERIFY_SUCCESS.getMessage())
                    .data(true)
                    .build();
        }
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.EMAIL_VERIFY_FAIL.getMessage())
                .data(false)
                .build();
    }

    @PostMapping("/register")
    public HttpResponse registerUser(@RequestBody UserSignUpDto userSignUpDto) {

        log.info("요청 json 파라미터 {}", userSignUpDto);

        boolean result = userSignUpService.registerUser(userSignUpDto);
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.USER_REGISTER_SUCCESS.getMessage())
                .data(result)
                .build();
    }

}
