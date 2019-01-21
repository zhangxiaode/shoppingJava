package com.zxd.shopping.controller;

import com.zxd.shopping.bean.WxUser;
import com.zxd.shopping.service.WxUserService;
import com.zxd.shopping.utils.ExceptionHandle;
import com.zxd.shopping.utils.ResultEnum;
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

import com.zxd.shopping.utils.ResultUtil;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public Object login2(HttpServletRequest request, @RequestBody String code) throws Exception{
        String jsontext="{\"name\":\"wjk\",\"age\":\"22\",\"love\":[{\"love1\":\"coding\",\"love2\":\"movie\"},{\"love1\":\"money\",\"love2\":\"NBA\"}]}";
        JSONObject m1 = JSON.parseObject(jsontext);
//        return ResultUtil.error(2001,"token失效");
//        throw new ExceptionHandle(ResultEnum.notoken);
//        return ResultUtil.success(m1);

        System.out.println(request.getSession().getAttribute("token"));
        Map<String, Object> response = new HashMap();
        response.put("token",request.getSession().getAttribute("token"));
        return ResultUtil.success(response);
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public Object login(HttpServletRequest request, @RequestParam(value = "code", required = true) String code, @RequestParam(value="encryptedData", required = true) String encryptedData, @RequestParam(value="iv", required = true) String iv) throws Exception {
        String url="https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grantType;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String sessionData = responseEntity.getBody();
        Map<String, String> login = new HashMap(JSON.parseObject(sessionData));

        Map<String, Object> result = new HashMap(JSON.parseObject(new String(this.decrypt(encryptedData, login.get("session_key"), iv),"UTF-8")));
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

        request.getSession().setAttribute("token",login.get("session_key"));
        System.out.println(request.getSession().getAttribute("token"));

        Map<String, Object> response = new HashMap();
        response.put("token",login.get("session_key"));
        response.put("userInfo",wxUser);
        return ResultUtil.success(response);
    }
}
