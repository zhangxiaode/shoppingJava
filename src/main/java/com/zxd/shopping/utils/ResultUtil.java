package com.zxd.shopping.utils;

public class ResultUtil {
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(2000);
        result.setMsg("请求成功");
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
