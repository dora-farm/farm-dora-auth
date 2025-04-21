package com.farmdora.farmdoraauth.auth.oauth.service;

import com.farmdora.farmdoraauth.auth.oauth.repository.SnsRepository;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginService {

    private final SnsRepository snsRepository;

    public User oauthLogin(String snsName) {

        return snsRepository.findUserBySnsName(snsName).orElseThrow(() -> new ResourceNotFoundException("소셜 로그인", snsName));
    }
}
