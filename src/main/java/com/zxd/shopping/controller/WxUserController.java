package com.zxd.shopping.controller;

import com.zxd.shopping.bean.Login;
import com.zxd.shopping.bean.Login2;
import com.zxd.shopping.bean.WxUser;
import com.zxd.shopping.bean.WxUser2;
import com.zxd.shopping.service.WxUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
import com.alibaba.fastjson.TypeReference;

import com.zxd.shopping.utils.ResultUtil;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@RestController
@RequestMapping("/apis")
@ComponentScan({"com.zxd.shopping.service"})
@MapperScan("com.zxd.shopping.mapper")
public class WxUserController {

    @Autowired
    private WxUserService wxUserService;
    private WxUser wxUser;

    // 算法名称
    final String KEY_ALGORITHM = "AES";

    // 加解密算法/模式/填充方式
    final String algorithmStr = "AES/CBC/PKCS7Padding";
    //
    private Key key;
    private Cipher cipher;

    public void init(byte[] keyBytes) {

        // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] decrypt(String encryptedDataStr, String keyBytesStr, String ivStr) {
        byte[] encryptedText = null;
        byte[] encryptedData = null;
        byte[] sessionkey = null;
        byte[] iv = null;

        try {
            sessionkey = Base64.decodeBase64(keyBytesStr);
            encryptedData = Base64.decodeBase64(encryptedDataStr);
            iv = Base64.decodeBase64(ivStr);

            init(sessionkey);

            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptedText;
    }



    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.grantType}")
    private String grantType;

    @RequestMapping(value="/login2", method = RequestMethod.POST)
    public Object login2(@RequestBody String code) throws Exception{
        String jsontext="{\"name\":\"wjk\",\"age\":\"22\",\"love\":[{\"love1\":\"coding\",\"love2\":\"movie\"},{\"love1\":\"money\",\"love2\":\"NBA\"}]}";
        JSONObject m1 = JSON.parseObject(jsontext);
//        return ResultUtil.error(300,"没有权限");
        return ResultUtil.success(m1);
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public Object login(@RequestParam(value = "code", required = true) String code, @RequestParam(value="encryptedData", required = true) String encryptedData, @RequestParam(value="iv", required = true) String iv) throws Exception {
        String url="https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grantType;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String sessionData = responseEntity.getBody();
        Login login = JSON.parseObject(sessionData, new TypeReference<Login>() {});

        String url2="https://api.weixin.qq.com/cgi-bin/token?appid=" + appId + "&secret=" + secret + "&grant_type=client_credential";
        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<String> responseEntity2 = restTemplate2.exchange(url2, HttpMethod.GET, null, String.class);
        String sessionData2 = responseEntity2.getBody();
        Login2 login2 = JSON.parseObject(sessionData2, new TypeReference<Login2>() {});

//        System.out.println("1111appId" + appId);
//        System.out.println("2222secret" + secret);
//        System.out.println("3333code" + code);
//        System.out.println("4444encryptedData" + encryptedData);
//        System.out.println("5555iv" + iv);

//        System.out.println("111Access_token:" + login2.getAccess_token());
//        System.out.println("222Openid:" + login2.getExpires_in());


//        String url3="https://api.weixin.qq.com/sns/userinfo?access_token=" + login2.getAccess_token() + "&openid=" + login.getOpenid() + "&lang=zh_CN";
//        RestTemplate restTemplate3 = new RestTemplate();
//        ResponseEntity<String> responseEntity3 = restTemplate3.exchange(url3, HttpMethod.GET, null, String.class);
//        String sessionData3 = responseEntity3.getBody();


        JSONObject result = JSON.parseObject(new String(this.decrypt(encryptedData, login.getSession_key(), iv),"UTF-8"));
        WxUser2 wxUser2 = JSON.parseObject(JSONObject.toJSON(result).toString(), new TypeReference<WxUser2>() {});
        wxUser = new WxUser();
        wxUser.setOpenid(wxUser2.getOpenId());
        wxUser.setNickname(wxUser2.getNickName());
        wxUser.setAvatarurl(wxUser2.getAvatarUrl());
        wxUser.setGender(wxUser2.getGender());
        wxUser.setCountry(wxUser2.getCountry());
        wxUser.setProvince(wxUser2.getProvince());
        wxUser.setCity(wxUser2.getCity());
        wxUser.setLanguage(wxUser2.getLanguage());
        wxUser.setUnionid(wxUser2.getUnionId());
        long timeStamp = System.currentTimeMillis();
        wxUser.setCtime(timeStamp);
//        wxUserService = new WxUserService();
        wxUserService.addUser(wxUser);
        System.out.println(wxUserService.findUser("aasdfsdafasf"));
        return ResultUtil.success(JSONObject.parseObject(JSONObject.toJSON(wxUser).toString()));
    }
}
