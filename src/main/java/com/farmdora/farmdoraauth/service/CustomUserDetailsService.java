package com.farmdora.farmdoraauth.service;

import com.farmdora.farmdoraauth.common.exception.AccessDeniedException;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.dto.CustomUserDetail;
import com.farmdora.farmdoraauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("로그인",username));
        log.info("차단여부 {}",user.isBlind());
        if (user.isBlind()) {
            throw new AccessDeniedException();
        }

        return new CustomUserDetail(user);
    }
}
