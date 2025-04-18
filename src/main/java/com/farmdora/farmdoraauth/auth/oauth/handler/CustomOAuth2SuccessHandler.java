package com.farmdora.farmdoraauth.auth.oauth.handler;

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
            String username = user.getId();
            String role = user.getAuth().getRole();

            String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L);

            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("블랙리스트에 등록된 토큰으로 로그인 시도 불가");
                return;
            }

            try {
                redisTemplate.opsForValue().set("accessToken:" + username, token, Duration.ofHours(5));
            } catch (Exception e) {
                e.printStackTrace();
            }

            authentication = new UsernamePasswordAuthenticationToken(username, null, jwtUtil.getAuthorities(token));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setContentType("application/json;charset=utf-8");
            Cookie cookie = new Cookie("jwt_token", token);
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
