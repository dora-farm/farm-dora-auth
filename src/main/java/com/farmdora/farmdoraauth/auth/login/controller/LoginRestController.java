package com.farmdora.farmdoraauth.auth.login.controller;

import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginRestController {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String COOKIES_NAME = "jwt_token";

    @PostMapping("/logout")
    public HttpResponse logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ResourceNotFoundException("JWT 쿠키", HttpStatus.UNAUTHORIZED);
        }

        String token = null;
        for (Cookie cookie : cookies) {
            if (COOKIES_NAME.equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) {
            throw new ResourceNotFoundException("JWT 쿠키", HttpStatus.UNAUTHORIZED);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "알 수 없음";

            // Redis 블랙리스트에 추가
            redisTemplate.opsForValue().set("blacklist:" + token, "logout", Duration.ofMinutes(30));
            log.info("로그아웃 처리 - 유저: {}, 토큰 블랙리스트 등록 완료", username);

            // Redis 캐시된 토큰 삭제
            redisTemplate.delete("accessToken:" + username);
            log.info("Redis 캐시 토큰 삭제: {}", username);

            // 시큐리티 컨텍스트 클리어
            SecurityContextHolder.clearContext();
            log.info("시큐리티 컨텍스트 초기화 완료");

            return HttpResponse.builder()
                    .status(200)
                    .message("로그아웃 성공하였습니다.")
                    .build();

        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생", e);
            return HttpResponse.builder()
                    .status(500)
                    .message("서버 오류로 로그아웃에 실패하였습니다.")
                    .build();
        }
    }
}
