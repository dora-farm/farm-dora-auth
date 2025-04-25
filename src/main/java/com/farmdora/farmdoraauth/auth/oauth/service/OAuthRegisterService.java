package com.farmdora.farmdoraauth.auth.oauth.service;

import com.farmdora.farmdoraauth.auth.oauth.repository.SnsRegisterRepository;
import com.farmdora.farmdoraauth.auth.oauth.repository.SnsTypeRepository;
import com.farmdora.farmdoraauth.entity.Sns;
import com.farmdora.farmdoraauth.entity.SnsType;
import com.farmdora.farmdoraauth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OAuthRegisterService {

    private final SnsRegisterRepository snsRegisterRepository;
    private final SnsTypeRepository snsTypeRepository;

    public void registerOAuth(int userId, String provider, String snsName) throws IOException {

        short typeId = snsTypeRepository.findByName(provider).getId();

        Sns sns = Sns.builder()
                .user(User.builder()
                        .userId(userId)
                        .build())
                .type(SnsType.builder()
                        .id(typeId)
                        .build())
                .snsName(snsName)
                .build();
            snsRegisterRepository.save(sns);
    }
}
