package com.farmdora.farmdoraauth.jwt;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.auth.login.dto.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = request.getParameter("id");
        String password = request.getParameter("pwd");

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드(여기서 JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {

        log.info("로그인 성공 시 실행");

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String username = customUserDetail.getUsername();
        int userId = userRepository.findUserIdById(username);
        log.info("로그인 유저의 프라이머리키 {}",userId);

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

        //응답에 jwt 토큰 반환
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", token);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(result);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(responseJson);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.info("로그인 실패");

        String errorMessage;

        if (failed instanceof BadCredentialsException) {
            log.info("비밀번호가 다릅니다.");

            errorMessage = "비밀번호가 다릅니다.";
        } else {
            errorMessage = "로그인에 실패 하였습니다.";
        }

        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(new HttpResponse(HttpStatus.UNAUTHORIZED, errorMessage, null)));
            response.getWriter().flush();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
