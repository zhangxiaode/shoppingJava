package com.zxd.shopping.exception;

public class WxUserException {
    private Integer code;

    public WxUserException(Integer code, String message) {
//        super(message);
//        this.code = code();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
