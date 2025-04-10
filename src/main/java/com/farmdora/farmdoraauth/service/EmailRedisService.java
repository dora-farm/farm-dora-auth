package com.farmdora.farmdoraauth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public EmailRedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 인증 코드 저장
    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES); // 5분 유효
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return code.equals(savedCode);
    }

    // 인증 코드 삭제
    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }

}
