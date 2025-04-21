package com.farmdora.farmdoraauth.auth.oauth.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Environment env;
    private static final String COOKIE_NAME = "jwt_token";
    private static final String REDIS_KEY = "frontFromToken";
    private static final String SOCIAL_PROVIDER_KEY = "provider";

    @PostMapping("/id/save")
    public HttpResponse idSave(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String provider = map.get(SOCIAL_PROVIDER_KEY);
        Cookie[] cookies = request.getCookies();
        String token = "";
        log.info("idSave {}", provider);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    token = cookie.getValue();
                }
            }
        }

        redisTemplate.opsForValue().set(REDIS_KEY, token, Duration.ofMinutes(5));

        String redirectUrl = env.getProperty("social.redirect.url") + provider;
        log.info("redirectUrl = {}", redirectUrl);
        return HttpResponse.builder()
                .message("아이디 저장성공")
                .data(redirectUrl)
                .build();
    }
}
