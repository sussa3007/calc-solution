package com.solution.calc.constant;

import lombok.Getter;

@Getter
public enum UserLevel {

    ADMIN("관리자"),
    OFFICE("기업 회원"),
    HEAD_OFFICE("부본사"),
    AGENT("에이전트");

    private final String level;

    UserLevel(String level) {
        this.level = level;
    }
}
