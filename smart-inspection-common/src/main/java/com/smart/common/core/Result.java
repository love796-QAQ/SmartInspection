package com.smart.common.core;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return restResult(null, SUCCESS, "Success");
    }

    public static <T> Result<T> success(T data) {
        return restResult(data, SUCCESS, "Success");
    }

    public static <T> Result<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> error() {
        return restResult(null, ERROR, "Error");
    }

    public static <T> Result<T> error(String msg) {
        return restResult(null, ERROR, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
