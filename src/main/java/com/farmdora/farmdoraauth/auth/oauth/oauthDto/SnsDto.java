package com.farmdora.farmdoraauth.auth.oauth.oauthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnsDto {

    private int userId;
    private short typeId;
    private String snsId;

}
