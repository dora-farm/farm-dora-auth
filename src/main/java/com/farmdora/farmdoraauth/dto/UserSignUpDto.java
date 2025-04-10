package com.farmdora.farmdoraauth.dto;

import com.farmdora.farmdoraauth.entity.Address;
import com.farmdora.farmdoraauth.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {

    private String name;
    private String id;
    private String pwd;
    private String phoneNum;
    private String email;
    private Short authId;
    private String accountNum;
    private LocalDate birth;
    private Gender sex;
    private Short bankId;
}
