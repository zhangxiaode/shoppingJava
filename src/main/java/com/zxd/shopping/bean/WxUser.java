package com.zxd.shopping.bean;

//import com.zxd.shopping.exception.WxUserException;

public class WxUser {
    private String openid;
    private String nickname;
    private String avatarurl;
    private Integer gender;
    private String country;
    private String province;
    private String city;
    private String language;
    private String unionid;
    private Long ctime;

    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAvatarurl() {
        return avatarurl;
    }
    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getUnionid() {
        return unionid;
    }
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    public Long getCtime() {
        return ctime;
    }
    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }
}

