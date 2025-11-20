package com.smart.common.exception;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;

    public BizException(String message) {
        this.message = message;
        this.code = 500;
    }

    public BizException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public BizException(String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = 500;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
