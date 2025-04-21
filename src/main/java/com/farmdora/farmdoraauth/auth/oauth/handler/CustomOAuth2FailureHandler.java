package com.farmdora.farmdoraauth.auth.oauth.handler;

import com.farmdora.farmdoraauth.auth.oauth.exception.CustomOAuth2Exception;
import com.farmdora.farmdoraauth.auth.oauth.service.OAuthRegisterService;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private final OAuthRegisterService oAuthRegisterService;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof CustomOAuth2Exception ex) {
            String provider = ex.getProvider();
            String snsName = ex.getSnsName();
            String token = ex.getToken();

            int userId = jwtUtil.getUserId(token);
            try {
                oAuthRegisterService.registerOAuth(userId, provider, snsName);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, jwtUtil.getAuthorities(token));
                log.info("소셜 연동 토큰 {}", token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.sendRedirect("http://localhost:5173");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FOUND);
                response.sendRedirect("http://localhost:5173/my/user/profile?error=oauthregister");
            }
        }
    }
}
