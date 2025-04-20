package com.farmdora.farmdoraauth.mypage.user.depot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepotDto {
    int userId;
    String deliveryName;
    String receiverName;
    String phoneNum;
    String postNum;
    String addr;
    String detailAddr;
    String require;
    boolean defaultAddr;
}
