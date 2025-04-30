package com.farmdora.farmdoraauth.auth.register.controller;

import com.farmdora.farmdoraauth.auth.register.dto.SellerRegisterDto;
import com.farmdora.farmdoraauth.auth.register.service.NCPStorageService;
import com.farmdora.farmdoraauth.auth.register.service.SellerRegisterService;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.auth.register.dto.UserRegisterDto;
import com.farmdora.farmdoraauth.auth.register.message.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.UserRegisterService;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/auth/register")
public class RegisterRestController {
    private final UserRegisterService userRegisterService;
    private final SellerRegisterService sellerRegisterService;
    private final NCPStorageService ncpStorageService;

    private static final String EMAIL_SUB = "이메일 인증";
    private static final String EMAIL_TITLE = "이메일 인증 요청";
    private static final String EMAIL_CONTENT = "안녕하세요! 아래의 인증 코드를 일력하여 인증을 완료하세요:";

    @GetMapping("/idcheck")
    public HttpResponse idCheck(@RequestParam("id") String id) {

        userRegisterService.idCheck(id);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.ID_CHECK_SUCCESS.getMessage())
                .data(null)
                .build();
    }

    @GetMapping("/emailcheck")
    public HttpResponse emailCheck(@RequestParam("email") String email) {
        userRegisterService.emailCheck(email);
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
        userRegisterService.sendVerificationEmail(email, EMAIL_SUB, EMAIL_TITLE, EMAIL_CONTENT);
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

        if(userRegisterService.verifyEmail(email, code)){
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
    public HttpResponse registerUser(@RequestBody UserRegisterDto userRegisterDto) {

        log.info("요청 파라미터 {}", userRegisterDto);

        boolean result = userRegisterService.registerUser(userRegisterDto);
        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.USER_REGISTER_SUCCESS.getMessage())
                .data(result)
                .build();
    }
    @PostMapping("/seller")
    public HttpResponse registerSeller(Principal principal, @RequestPart("seller") SellerRegisterDto sellerRegisterDto,
                                       @RequestPart("file") MultipartFile file) {

        Integer userId = Integer.parseInt(principal.getName());

        sellerRegisterService.registerSeller(userId, sellerRegisterDto, file);

        return HttpResponse.builder()
                .status(200)
                .message(StandardRegisterMassage.SELLER_REGISTER_SUCCESS.getMessage())
                .data(true)
                .build();
    }

}


