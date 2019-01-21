package com.zxd.shopping.utils;

public enum ResultEnum {
    unknow_error(-1, "未知错误"),
    notoken(2001, "token失效")

    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
