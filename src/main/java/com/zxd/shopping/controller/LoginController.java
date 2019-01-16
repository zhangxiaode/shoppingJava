package com.zxd.shopping.controller;

import com.zxd.shopping.bean.Login;
import org.springframework.beans.factory.annotation.Value;
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
import com.alibaba.fastjson.TypeReference;

import com.zxd.shopping.utils.ResultUtil;

@RestController
@RequestMapping("/apis")
public class LoginController {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.grantType}")
    private String grantType;

    @RequestMapping(value="/login2", method = RequestMethod.POST)
    public Object login2(@RequestBody String code) throws Exception{
        System.out.println(code);
        String jsontext="{\"name\":\"wjk\",\"age\":\"22\",\"love\":[{\"love1\":\"coding\",\"love2\":\"movie\"},{\"love1\":\"money\",\"love2\":\"NBA\"}]}";
        JSONObject m1=new JSONObject();
        m1=JSON.parseObject(jsontext);
//        return ResultUtil.success(m1);
        Integer mycode=300;
        String msg="没有权限";
        return ResultUtil.error(mycode,msg);
//        return m1;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "code", required = false) String code){
        System.out.println(appId);
        System.out.println(secret);
        System.out.println(code);
        String url="https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grantType;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String sessionData = responseEntity.getBody();
        Login login = JSON.parseObject(sessionData, new TypeReference<Login>() {});
        System.out.println("111" + login.getOpenid());
        System.out.println("222" + login.getSession_key());
        return login.getOpenid();
    }
}
