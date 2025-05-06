package com.farmdora.farmdoraauth.jwt;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.auth.login.dto.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("id");
        String password = request.getParameter("pwd");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
        log.info("로그인 성공 시 실행");

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String username = customUserDetail.getUsername();
        int userId = userRepository.findUserIdById(username);
        log.info("로그인 유저의 프라이머리키 {}", userId);



        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().iterator().next().getAuthority();

        String token = jwtUtil.createJwt(userId, role, username, 60 * 60 * 10L * 1000);

        if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("블랙리스트에 등록된 토큰으로 로그인 시도 불가");
            return;
        }

        try {
            redisTemplate.opsForValue().set("accessToken:" + userId, token, Duration.ofHours(5));
        } catch (Exception e) {
            log.error("레디스 저장 오류 {}", e.getMessage());
        }

        // Set-Cookie로 토큰 내려주기 (HttpOnly, Secure 개발환경 false)
        Cookie jwtCookie = new Cookie("jwt_token", token);
        jwtCookie.setHttpOnly(false); // client js에서 쿠키 접근 가능, true일 경우 불가능
        jwtCookie.setSecure(false); // 배포 환경에선 true
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) Duration.ofHours(3).getSeconds());

        response.addCookie(jwtCookie);

        // 성공 메시지만 내려주기 (토큰은 쿠키로 처리)
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"message\": \"로그인 성공\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.info("로그인 실패");

        String errorMessage;
        if (failed instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 다릅니다.";
        } else if (failed instanceof CredentialsExpiredException) {
            errorMessage = "탈퇴한 회원입니다.";
        } else if (failed instanceof DisabledException) {
            errorMessage = "차단된 회원입니다.";
        } else {
            errorMessage = "로그인에 실패하였습니다.";
        }

        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(new HttpResponse(HttpStatus.UNAUTHORIZED, errorMessage, null))
            );
            response.getWriter().flush();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
