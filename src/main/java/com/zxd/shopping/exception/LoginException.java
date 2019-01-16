package com.zxd.shopping.exception;

public class LoginException {
    private Integer code;

    public LoginException(Integer code,String message) {
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
