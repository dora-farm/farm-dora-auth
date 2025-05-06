package com.farmdora.farmdoraauth.auth.find.dto;

import com.farmdora.farmdoraauth.entity.Auth;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

    private String id;
    private String role;
}
