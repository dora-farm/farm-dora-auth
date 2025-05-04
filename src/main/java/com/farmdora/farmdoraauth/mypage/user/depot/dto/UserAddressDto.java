package com.farmdora.farmdoraauth.mypage.user.depot.dto;

import com.farmdora.farmdoraauth.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class UserAddressDto {
    private String name;
    private String phoneNum;
    private Address address;

    public UserAddressDto(String name, String phoneNum, Address address) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
    }
}
