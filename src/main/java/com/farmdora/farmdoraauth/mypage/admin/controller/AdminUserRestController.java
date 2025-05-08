package com.farmdora.farmdoraauth.mypage.admin.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.mypage.admin.dto.BlindRequestDto;
import com.farmdora.farmdoraauth.mypage.admin.dto.SellerApprovalDto;
import com.farmdora.farmdoraauth.mypage.admin.message.AdminUserMessage;
import com.farmdora.farmdoraauth.mypage.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/mypage/admin/user")
public class AdminUserRestController {

    private final AdminUserService adminUserService;

    @PatchMapping("/blind")
    public ResponseEntity<?> blindUser(@RequestBody BlindRequestDto requestDto) {
        String message = adminUserService.blindUser(requestDto.getUserId());

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, message, true));
    }

    @GetMapping("/approval/request")
    public ResponseEntity<?> approvalRequestUser(){
       List<SellerApprovalDto> sellerList = adminUserService.approvalRequestUser();

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, AdminUserMessage.GET_SELLER_APPROVAL_LIST.getMessage(), sellerList));
    }

    @PatchMapping("/approval/{sellerId}")
    public ResponseEntity<?> approvalUser(@PathVariable int sellerId){
        adminUserService.approveUser(sellerId);

        String message = AdminUserMessage.SELLER_APPROVE_SUCCESS.getMessage();

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, message, true));
    }
}