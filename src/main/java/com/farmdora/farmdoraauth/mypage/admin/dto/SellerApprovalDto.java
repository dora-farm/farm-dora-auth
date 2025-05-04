package com.farmdora.farmdoraauth.mypage.admin.dto;

import com.farmdora.farmdoraauth.entity.Address;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerApprovalDto {

    private String userId;
    private String username;
    private int sellerId;
    private String name;
    private String companyNum;
    private String phoneNum;
    private Address address;
    private String saveFile;

}
