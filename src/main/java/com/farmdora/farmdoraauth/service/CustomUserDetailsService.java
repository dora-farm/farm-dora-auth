package com.farmdora.farmdoraauth.service;

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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            User loginUser = user.get();
            log.info("User : {}", loginUser);
            return new CustomUserDetail(loginUser);
        }
        return null;
    }
}
