package com.farmdora.farmdoraauth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG.HS512.key()
                        .build()
                        .getAlgorithm());
    }

    public Integer getUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().get("userId", Integer.class);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
       String role = Jwts.parser()
               .verifyWith(secretKey)
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .get("role", String.class);
       return List.of(new SimpleGrantedAuthority(role));
    }

    public boolean validateToken(String token) {
        try{
           Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token).getPayload();
            log.info("토큰 유효성 {}",!claims.getExpiration().before(new Date()));
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Jwt 요효성 검사 실패: {}" , e.getMessage());
            return false;
        }

    }

    public String createJwt(int userId, String role, Long expiredMs){
        return Jwts.builder()
                .claim("userId",userId)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
