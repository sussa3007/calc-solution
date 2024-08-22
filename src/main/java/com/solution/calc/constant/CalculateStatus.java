package com.solution.calc.constant;

import lombok.Getter;

@Getter
public enum CalculateStatus {

    WAIT("대기"),
    COMPLETE("완료"),
    CANCEL("취소");

    private final String status;

    CalculateStatus(String status) {
        this.status = status;
    }
}
