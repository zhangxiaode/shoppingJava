package com.zxd.shopping.bean;

//import com.zxd.shopping.exception.LoginException;

public class Login {
    private String openid;
    private String session_key;

    public String getOpenid() throws Exception {
//        throw new LoginException(100,"你还在上小学吧");
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
}