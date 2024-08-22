package com.solution.calc.constant;

import lombok.Getter;

@Getter
public enum DepositStatus {

    WAIT("대기"),
    DEPOSIT_COMPLETE("입금 완료"),
    COMPLETE("처리 완료"),
    CANCEL("취소");

    private final String status;

    DepositStatus(String status) {
        this.status = status;
    }
}
