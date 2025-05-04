package com.farmdora.farmdoraauth.mypage.admin.mapper;

import com.farmdora.farmdoraauth.entity.Seller;
import com.farmdora.farmdoraauth.mypage.admin.dto.SellerApprovalDto;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {
    public SellerApprovalDto toSellerApprovalDto(Seller seller) {
        return SellerApprovalDto.builder()
                .userId(seller.getUser().getId())
                .username(seller.getUser().getName())
                .sellerId(seller.getId())
                .name(seller.getName())
                .companyNum(seller.getCompanyNum())
                .phoneNum(seller.getPhoneNum())
                .address(seller.getAddress())
                .saveFile(seller.getSaveFile())
                .build();
    }
}
