package com.farmdora.farmdoraauth.mypage.admin.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminUserMessage {
    USER_BLIND("유저 블라인드 성공"),
    USER_UNBLIND("유저 블라인드 취소 성공")
    ;
    private final String message;
}
