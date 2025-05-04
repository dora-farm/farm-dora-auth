package com.farmdora.farmdoraauth.mypage.admin.service;

import com.farmdora.farmdoraauth.auth.register.repository.AuthRepository;
import com.farmdora.farmdoraauth.auth.register.repository.SellerRepository;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.Auth;
import com.farmdora.farmdoraauth.entity.Seller;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.admin.dto.SellerApprovalDto;
import com.farmdora.farmdoraauth.mypage.admin.mapper.SellerMapper;
import com.farmdora.farmdoraauth.mypage.admin.message.AdminUserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final AuthRepository authRepository;

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

    public List<SellerApprovalDto> approvalRequestUser(){

        List<Seller> sellerList = sellerRepository.findByIsApproveOrderByIdDesc(false);

        return sellerList.stream()
                .map(sellerMapper::toSellerApprovalDto)
                .toList();
    }

    public void approveUser(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다: " + sellerId));

        // Seller 승인 처리 (setter 대신 의미 있는 메서드)
        seller.approveSeller();

        // User 권한 변경
        User user = seller.getUser();
        Auth newAuth = authRepository.findById((short) 2)
                .orElseThrow(() -> new IllegalArgumentException("Auth ID 2를 찾을 수 없습니다"));
        user.changeAuth(newAuth);
    }

}
