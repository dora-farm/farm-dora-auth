package com.farmdora.farmdoraauth.dto;

import com.farmdora.farmdoraauth.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetail implements UserDetails {

    private final User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add((GrantedAuthority) () -> user.getAuth().getRole());
//        log.info("Authorities : {}", authorities);
//
//        return authorities;

        return Collections.singleton(()->user.getAuth().getRole());
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public boolean isEnabled() {
        return !user.isBlind();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
