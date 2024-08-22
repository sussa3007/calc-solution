package com.solution.calc.constant;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("활성화"),
    BLOCK("차단"),
    INACTIVE("탈퇴");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

}
