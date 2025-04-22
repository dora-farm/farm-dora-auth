package com.farmdora.farmdoraauth.auth.login.dto;

import com.farmdora.farmdoraauth.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Getter
public class CustomUserDetail implements UserDetails {

    private final Integer userId;
    private final String username;
    private final String password;
    private final String role;
    private final boolean isBlind;

    public CustomUserDetail(Integer userId, String username, String password, String role, boolean isBlind) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isBlind = isBlind;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> role);
    }

    @Override
    public boolean isEnabled() {
        return !isBlind;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}

