package com.zxd.shopping.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result Handle(Exception e){

//        if (e instanceof GrilException){
//            GrilException grilException = (GrilException) e;
//            return ResultUtil.error(grilException.getCode(),grilException.getMessage());
//
//        }else {
//　　　　　　//将系统异常以打印出来
//            logger.info("[系统异常]{}",e);
//            return ResultUtil.error(-1,"未知错误");
//        }
        return ResultUtil.error(-1,"未知错误");

    }
}
