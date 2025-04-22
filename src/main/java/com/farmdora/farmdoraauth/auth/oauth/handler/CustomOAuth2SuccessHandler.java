package com.farmdora.farmdoraauth.auth.oauth.handler;

import com.farmdora.farmdoraauth.auth.login.RedisString.RedisKey;
import com.farmdora.farmdoraauth.auth.login.dto.CustomUserDetail;
import com.farmdora.farmdoraauth.auth.oauth.service.OAuthLoginService;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final OAuthLoginService oAuthLoginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String snsName = (String) oAuth2User.getAttributes().get("snsName");
            String provider = (String) oAuth2User.getAttributes().get("provider");

            log.info("snsName: {}, provider: {}", snsName, provider);

            User user = oAuthLoginService.oauthLogin(snsName);
            CustomUserDetail customUserDetail = new CustomUserDetail(
                    user.getUserId(),
                    user.getId(),
                    user.getPwd(),
                    user.getAuth().getRole(),
                    user.isBlind()
            );

            String token = jwtUtil.createJwt(customUserDetail, 60 * 60 * 10L);

            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKey.blackList + token))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("블랙리스트에 등록된 토큰으로 로그인 시도 불가");
                return;
            }

            try {
                redisTemplate.opsForValue().set(RedisKey.accessToken + user.getId(), token, Duration.ofHours(5));
            } catch (Exception e) {
                e.printStackTrace();
            }

            authentication = new UsernamePasswordAuthenticationToken(user.getId(), null, jwtUtil.getAuthorities(token));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setContentType("application/json;charset=utf-8");
            Cookie cookie = new Cookie(RedisKey.cookieName, token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setDomain("localhost");
            cookie.setMaxAge(60 * 60 * 5);
            response.addCookie(cookie);
            response.sendRedirect("http://localhost:5173/");
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.sendRedirect("http://localhost:5173/login?error=oauthLogin");
        }
    }
}
