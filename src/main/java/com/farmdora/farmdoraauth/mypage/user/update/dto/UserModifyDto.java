package com.farmdora.farmdoraauth.mypage.user.update.dto;

import com.farmdora.farmdoraauth.entity.Address;
import com.farmdora.farmdoraauth.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyDto {
    private String pwd;
    private String phoneNum;
    private String email;
    private String accountNum;
    private LocalDate birth;
    private Gender sex;
    private short bankId;
    private Address address;
}
