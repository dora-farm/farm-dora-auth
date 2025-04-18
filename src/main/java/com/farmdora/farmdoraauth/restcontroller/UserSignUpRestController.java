package com.farmdora.farmdoraauth.restcontroller;

import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.dto.CustomUserDetail;
import com.farmdora.farmdoraauth.dto.UserSignUpDto;
import com.farmdora.farmdoraauth.responseMessage.UserRegisterMassage;
import com.farmdora.farmdoraauth.service.UserSignUpService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserSignUpRestController {
    private final UserSignUpService userSignUpService;
    private final RedisTemplate<String, String> redisTemplate;

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
    public HttpResponse sendEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        log.info(email);
        userSignUpService.sendVerificationEmail(email);
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.EMAIL_SEND_SUCCESS.getMessage())
                .data(true)
                .build();
    }

    @PostMapping("/verify/email")
    public HttpResponse verifyEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        String code = emailBody.get("code");

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
    public HttpResponse registerUser(@ModelAttribute UserSignUpDto userSignUpDto) {

        log.info("요청 파라미터 {}", userSignUpDto);

        boolean result = userSignUpService.registerUser(userSignUpDto);
        return HttpResponse.builder()
                .status(200)
                .message(UserRegisterMassage.USER_REGISTER_SUCCESS.getMessage())
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    public HttpResponse logout(@RequestHeader("Authorization") String authorizationHeader) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         Object principal = authentication.getPrincipal();
         String username = principal.toString();
         log.info("유저네임{}", username);
         log.info("Authorization header: {}", authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // "Bearer " 이후의 JWT 추출
            log.info("Authorization header: {}", authorizationHeader);
            try {
                // 블랙리스트 추가
                redisTemplate.opsForValue().set("blacklist:" + token, "logout", Duration.ofMinutes(5));
                log.info("블랙리스트 추가: {}", token);
                // 기존 토큰 삭제
                redisTemplate.delete("accessToken:" + username);
                log.info("레디스 삭제");
                // SecurityContext 초기화
                SecurityContextHolder.clearContext();
                log.info("시큐리티 컨텍트 홀더 초기화");

            } catch (Exception e) {
                e.printStackTrace();
                log.info("로그아웃 실패");
//                return ResponseEntity.internalServerError().body("로그아웃 실패");
            }
        } else {
            throw new ResourceNotFoundException("Authorization 헤더", HttpStatus.UNAUTHORIZED);
        }
        return HttpResponse.builder().status(200).message("로그아웃 성공하였습니다.").build();
    }
}


