package com.zxd.shopping.controller;

import com.zxd.shopping.bean.WxUser;
import com.zxd.shopping.service.WxUserService;
import com.zxd.shopping.utils.Encrypt;
import com.zxd.shopping.utils.RedisUtil;
import com.zxd.shopping.utils.ResultUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

@RestController
@RequestMapping("/apis")
@ComponentScan({"com.zxd.shopping.service"})
@MapperScan("com.zxd.shopping.mapper")
public class WxUserController {

    @Autowired
    private WxUserService wxUserService;
    private WxUser wxUser;

//    @Resource
//    private RedisTemplate redisTemplate;
    @Resource
    RedisUtil redisUtil;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.grantType}")
    private String grantType;

    Encrypt encrypt = new Encrypt();
    String salt = "zxdkxljfnx";

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public Object login(@RequestParam(value = "code", required = true) String code, @RequestParam(value="encryptedData", required = true) String encryptedData, @RequestParam(value="iv", required = true) String iv) throws Exception {
        String url="https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grantType;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String sessionData = responseEntity.getBody();
        Map<String, String> login = new HashMap(JSON.parseObject(sessionData));

        Map<String, Object> result = new HashMap(JSON.parseObject(new String(encrypt.decrypt(encryptedData, login.get("session_key"), iv),"UTF-8")));
        wxUser = new WxUser();
        wxUser.setOpenid((String) result.get("openId"));
        wxUser.setNickname((String) result.get("nickName"));
        wxUser.setAvatarurl((String) result.get("avatarUrl"));
        wxUser.setGender((Integer) result.get("gender"));
        wxUser.setCountry((String) result.get("country"));
        wxUser.setProvince((String) result.get("province"));
        wxUser.setCity((String) result.get("city"));
        wxUser.setLanguage((String) result.get("language"));
        wxUser.setUnionid((String) result.get("unionId"));
        wxUser.setCtime(System.currentTimeMillis());
        if(wxUserService.findUser(wxUser.getOpenid())==null) {
            wxUserService.addUser(wxUser);
        } else {
            wxUserService.updateUser(wxUser);
        }

        /**
         * 由oppenid + session_key + salt 生成自定义登陆态token.
         */

        String plaintext = wxUser.getOpenid() + "," + login.get("session_key");
        String aesEncode = encrypt.AESEncode(salt, plaintext);
        redisUtil.set("token", aesEncode);

        Map<String, Object> response = new HashMap();
        response.put("token",aesEncode);
        response.put("userInfo",wxUser);
        return ResultUtil.success(response);
    }

    @RequestMapping(value="/login2", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public Object login2(@RequestBody Map<String, Object> params) throws Exception{
        String token = (String) params.get("token");
        String aesDncode = encrypt.AESDncode(salt, token);
        String[] oppSes=aesDncode.split(",");
        String oppenid = oppSes[0];
        Map<String, String> res = new HashMap();
        res.put("oppenid",oppenid);
        return ResultUtil.success(res);
    }
}
