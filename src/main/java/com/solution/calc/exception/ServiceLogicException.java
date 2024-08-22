package com.solution.calc.exception;

import com.solution.calc.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ServiceLogicException extends RuntimeException {

    private final ErrorCode errorCode;

    public ServiceLogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceLogicException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
