package com.solution.calc.constant;

import lombok.Getter;

@Getter
public enum LogType {

    USER_MONEY_COLLECT("[회원 잔액 회수]"),
    USER_MONEY_PAYMENT("[회원 잔액 지급]"),
    DEPOSIT_COMPLETE_CANCEL("[입금 완료 건 취소]"),
    INSPECTION_ACTIVE("[점검중 활성화]"),
    INSPECTION_INACTIVE("[점검중 비활성화]");

    private final String  message;


    LogType(String message) {
        this.message = message;
    }

    public static String createInfoLog(String activeUser, String targetUser, LogType logType) {
        String log = activeUser +
                "님이 " +
                targetUser +
                "님에 대한" +
                logType.getMessage() +
                " 작업을 하였습니다\n";
        return log;
    }

    public static String createSetLog(String activeUser, LogType logType) {
        String log = activeUser +
                "님이 " +
                logType.getMessage() +
                " 작업을 하였습니다\n";
        return log;
    }

    public static String createMoneyLog(String activeUser, String targetUser,
                                        LogType logType, long balance,
                                        long targetUserBalance,
                                        long resultTargetUserBalance
    ) {

        return activeUser +
                "님이 " +
                targetUser +"["+targetUserBalance +" -> "+ resultTargetUserBalance+"]"+
                "님에 대한" +
                logType.getMessage() +
                ". 요청 금액 = "+balance+"\n";
    }

    public static String createCancelLog(String activeUser,
                                        LogType logType,
                                        String targetUsername,
                                         String txnid
    ) {

        return activeUser +
                "님이 " +
                targetUsername +"["+txnid+"]"+
                "건 대한" +
                logType.getMessage() +
                ".";
    }
}
