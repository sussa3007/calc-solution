package com.solution.calc.constant;

import lombok.Getter;

public enum ErrorCode {

    BAD_REQUEST(400, "BAD REQUEST", 14001),
    BAD_REQUEST_DEPOSIT_CANCEL(400, "BAD REQUEST Deposit Cancel - 현재 잔액보다 요청 잔액이 많을수 없습니다", 14001),
    USER_EXIST(400, "USER EXIST", 14002),

    ACCESS_DENIED(403, "ACCESS DENIED", 14003),
    EXPIRED_ACCESS_TOKEN(403, "Expired Access Token", 14004),
    NOT_FOUND(404, "NOT FOUND", 14005),
    NOT_FOUND_COOKIE(404, "NOT FOUND COOKIE", 14006),
    ACCESS_DENIED_REQUEST_API(403, "ACCESS_DENIED_REQUEST_API", 14007),
    ARGUMENT_MISMATCH_BAD_REQUEST(400, "ARGUMENT_MISMATCH_BAD_REQUEST", 14008),
    BLOCK_OR_INACTIVE_USER(403, "차단 또는 탈퇴 회원 입니다.", 14009),
    USER_INACTIVE(403, "User Inactive" , 14010),

    NOT_FOUND_USER(404, "회원을 찾을수 없습니다.", 14011),
    WRONG_PASSWORD(403, "비밀번호가 틀렸습니다.", 14013),
    ACCOUNT_EXIST(400, "계좌 중복입니다.", 14014),
    WITHDRAW_BAD_REQUEST(400, "정산 신청 금액은 보유 금액보다 작아야 합니다.", 14015),
    RATE_BAD_REQUEST(400, "하위 회원은 상위 회원 요율보다 작아야 합니다.", 14016),
    BAD_REQUEST_CALCULATE_EXIST(400, "이미 요청된 정산 데이터가 있습니다.", 14017),
    BAD_REQUEST_DEPOSIT_BALANCE(400, "최소 요청 10,000 혹은 10,000 단위 요청만 가능합니다.", 14018),
    EXCEL_IO_ERROR(400, "Excel IO Error.", 14019),
    EXCEL_DATA_EMPTY(400, "조회된 데이터 없음.", 14020),
    BAD_REQUEST_MONEY_COLLECT(400, "기존 금액보다 많을수 없습니다.", 14020),
    BAD_REQUEST_DEPOSIT_EXIST(400, "이미 입금신청 내역이 존재합니다.", 14021),
    BAD_REQUEST_DEPOSIT_COMPLETE(400, "이미 완료된 입금 신청 내역입니다.", 14022),
    BAD_REQUEST_DEPOSIT_INACTIVE(400, "입금 신청이 비활성화 상태입니다.", 14023),







    INTERNAL_SERVER_ERROR(500, "Internal Server Error" , 15001),
    DATA_ACCESS_ERROR(500, "Data Access Error", 15002),
    NOT_IMPLEMENTED(501, "NOT IMPLEMENTED", 15003);

    @Getter
    private final int status;


    @Getter
    private final int code;

    @Getter
    private final String message;

    ErrorCode(int status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
