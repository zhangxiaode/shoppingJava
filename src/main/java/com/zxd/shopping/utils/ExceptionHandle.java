package com.zxd.shopping.utils;

public class ExceptionHandle extends RuntimeException {
    private Integer code;

    public ExceptionHandle(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
