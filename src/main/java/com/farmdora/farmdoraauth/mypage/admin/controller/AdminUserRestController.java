package com.farmdora.farmdoraauth.mypage.admin.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.mypage.admin.dto.BlindRequestDto;
import com.farmdora.farmdoraauth.mypage.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage/admin/user")
public class AdminUserRestController {

    private final AdminUserService adminUserService;

    @PatchMapping("/blind")
    public HttpResponse blindUser(@RequestBody BlindRequestDto requestDto) {
        String message = adminUserService.blindUser(requestDto.getUserId());
        return HttpResponse.builder()
                .status(200)
                .message(message)
                .data(true)
                .build();
    }

}
