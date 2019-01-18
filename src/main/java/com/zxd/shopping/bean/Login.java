package com.zxd.shopping.bean;

//import com.zxd.shopping.exception.WxUserException;

public class Login {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    /**
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40029	code 无效
     * 45011	频率限制，每个用户每分钟100次
     */
    private String errmsg;

    public String getOpenid() throws Exception {
//        throw new WxUserException(100,"你还在上小学吧");
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getSession_key() {
        return session_key;
    }
    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
    public String getUnionid() {
        return unionid;
    }
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    public Integer getErrcode() {
        return errcode;
    }
    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}