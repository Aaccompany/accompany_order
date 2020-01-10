package com.accompany.order.util;

public class ResultCode {


    public static final ResultCode SUCCESS = new ResultCode("操作成功", 0);
    public static final ResultCode FAILURE = new ResultCode("系统错误", -500);
    public static final ResultCode PARAM_ILLEGAL = new ResultCode("参数不合法", -2);
    public static final ResultCode OPERATOR_UNSUPPORT = new ResultCode("操作不支持", -3);
    public static final ResultCode PERMISSION_DENIED = new ResultCode("权限不足", -403);
    public static final ResultCode NEED_LOGIN = new ResultCode(" 需要登录", -401);
    private int code;
    private String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public ResultCode modifyMessage(String message) {
        this.message = message;
        return this;
    }

    protected ResultCode(int code, String message) {
        if (code <= 10000) {
            throw new RuntimeException("自定义ResultCode的错误码值必须大于10000");
        } else {
            this.code = code;
            this.message = message;
        }
    }

    private ResultCode(String message, int code) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }


}
