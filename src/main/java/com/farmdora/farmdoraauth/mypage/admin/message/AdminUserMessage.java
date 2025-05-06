package com.farmdora.farmdoraauth.mypage.admin.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminUserMessage {
    GET_SELLER_APPROVAL_LIST("판매자 승인 목록 출력 성공"),
    USER_BLIND("차단 성공하였습니다"),
    USER_UNBLIND("차단 해제 성공하였습니다."),
    SELLER_APPROVE_SUCCESS("승인되었습니다."),
    SELLER_APPROVE_FAILURE("승인 실패 하였습니다.")
    ;
    private final String message;
}
