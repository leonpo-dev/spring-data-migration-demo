package com.datasync.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Leonpo
 * @since 2025-10-16
 */
@Getter
@Setter
public class BusinessRuntimeException extends RuntimeException {


    private ErrorCode errorCode;


    public BusinessRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
