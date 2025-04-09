package com.farmdora.farmdoraauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE((byte)1), FEMALE((byte)2);

    private final byte value;
}
