package com.farmdora.farmdoraauth.auth.oauth.service;

import com.farmdora.farmdoraauth.auth.StringKey.StringKey;
import com.farmdora.farmdoraauth.auth.oauth.exception.CustomOAuth2Exception;
import com.farmdora.farmdoraauth.auth.oauth.factory.OAuthUserInfoFactory;
import com.farmdora.farmdoraauth.auth.oauth.oauthDto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthUserInfo oAuthUserInfo = OAuthUserInfoFactory.getOAuthUserInfo(provider, attributes);
        String snsName = oAuthUserInfo.getProvider() + "_" + oAuthUserInfo.getProviderId();

        Object frontFromTokenObj = redisTemplate.opsForValue().get(StringKey.frontFromToken);

        redisTemplate.delete(StringKey.frontFromToken);
        log.info("frontFromId: {}", frontFromTokenObj);

        if (frontFromTokenObj != null) {
            throw new CustomOAuth2Exception("연동 필요", snsName ,provider, String.valueOf(frontFromTokenObj));
        }

        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("snsName", snsName);
        customAttributes.put("provider", provider);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST")),
                customAttributes,
                "snsName" // ID 대신 snsName 으로 꺼내쓸 수 있음
        );
    }
}

