package com.farmdora.farmdoraauth.mypage.user.update.controller;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.jwt.JwtFromCookie;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserModifyDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserSelectDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.VerifyPasswordDto;
import com.farmdora.farmdoraauth.mypage.user.update.message.UserUpdateMassage;
import com.farmdora.farmdoraauth.mypage.user.update.service.UserUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Transactional
@RestController
@RequestMapping("/api/mypage/user/update")
@RequiredArgsConstructor
public class UserRestController {

    private final UserUpdateService userUpdateService;
    private final JwtUtil jwtUtil;

    @PostMapping("/verify")
    public HttpResponse verifyPassword(@RequestBody VerifyPasswordDto password) {
//        String token = JwtFromCookie.extractTokenFromCookie(request);
//        int userId = jwtUtil.getUserId(token);
        int userId=17;
        if (userUpdateService.verifyPassword(userId, password.getPassword())) {
        return HttpResponse.builder()
                .status(200)
                .message(UserUpdateMassage.PASSWORD_VERIFY_SUCCESS.getMessage())
                .data(true)
                .build();
        } else {
            return HttpResponse.builder()
                    .status(200)
                    .message(UserUpdateMassage.PASSWORD_VERIFY_FAILURE.getMessage())
                    .data(false)
                    .build();
        }
    }
    @GetMapping("/detail")
    public HttpResponse detail(HttpServletRequest request) {
//        String token = JwtFromCookie.extractTokenFromCookie(request);
//        int userId = jwtUtil.getUserId(token);
        int userId=17;
        UserSelectDto user = userUpdateService.getUserById(userId);
        return HttpResponse.builder()
                .status(200)
                .message(UserUpdateMassage.USER_SELECT_SUCCESS.getMessage())
                .data(user)
                .build();
    }

    @PutMapping("/modify")
    public HttpResponse modifyUser(@RequestBody UserModifyDto userModifyDto, HttpServletRequest request) {
//        String token = JwtFromCookie.extractTokenFromCookie(request);
//        int userId = jwtUtil.getUserId(token);
        int userId = 17;
        try{
            userUpdateService.updateUser(userId, userModifyDto);
            return HttpResponse.builder()
                    .status(200)
                    .message(UserUpdateMassage.USER_MODIFY_SUCCESS.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            return HttpResponse.builder()
                    .status(200)
                    .message(UserUpdateMassage.USER_MODIFY_FAILURE.getMessage())
                    .data(false)
                    .build();
        }
    }

    @PutMapping("/expire")
    public HttpResponse blindUser(HttpServletRequest request) {
//        String token = JwtFromCookie.extractTokenFromCookie(request);
//        int userId = jwtUtil.getUserId(token);
        int userId = 17;
        userUpdateService.blindUser(userId);
        return HttpResponse.builder()
                .status(200)
                .message(UserUpdateMassage.USER_EXPIRE_SUCCESS.getMessage())
                .data(true)
                .build();
    }
}
