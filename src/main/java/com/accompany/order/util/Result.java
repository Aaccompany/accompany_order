package com.accompany.order.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@Data
@NoArgsConstructor
public class  Result <T>{
    private T data;
    private int code;
    private String message = "";

    public Result(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
        return result;
    }

    public static Result success() {
        Result result = new Result((Object)null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        Result<T> result = new Result((Object)null, resultCode.getCode(), resultCode.getMessage());
        return result;
    }

    public static <T> Result<T> fail(T data, ResultCode resultCode) {
        Result<T> result = new Result(data, resultCode.getCode(), resultCode.getMessage());
        return result;
    }

}
