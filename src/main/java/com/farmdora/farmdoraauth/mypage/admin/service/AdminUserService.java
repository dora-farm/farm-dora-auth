package com.farmdora.farmdoraauth.mypage.admin.service;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.admin.message.AdminUserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public String blindUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("사용자 차단", userId));

        if(user.isBlind()){
            user.unblindUser();
            return AdminUserMessage.USER_UNBLIND.getMessage();
        }else {
            user.blindUser();
            return AdminUserMessage.USER_BLIND.getMessage();
        }
    }

}
