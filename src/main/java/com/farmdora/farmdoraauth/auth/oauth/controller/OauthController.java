package com.farmdora.farmdoraauth.auth.oauth.controller;

import com.farmdora.farmdoraauth.auth.StringKey.StringKey;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/id/save")
    public HttpResponse idSave(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String provider = map.get(StringKey.provider);
        String token = jwtUtil.extractTokenFromCookie(request);

        log.info("idSave {}", provider);

        redisTemplate.opsForValue().set(StringKey.frontFromToken, token, Duration.ofMinutes(5));

        String redirectUrl = env.getProperty("social.redirect.url") + provider;
        log.info("redirectUrl = {}", redirectUrl);
        return HttpResponse.builder()
                .message("아이디 저장성공")
                .data(redirectUrl)
                .build();
    }
}
