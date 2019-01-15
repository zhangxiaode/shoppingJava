package com.zxd.shopping.controller;

import com.zxd.shopping.bean.User;
import com.zxd.shopping.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
@ComponentScan({"com.zxd.shopping.service"})
@MapperScan("com.zxd.shopping.mapper")
public class UserController {

    @Autowired
    private UserService userService;
    private User user;

    @RequestMapping("/findUser")
    public User findUser(){
        User user = userService.findUser(1);
        return user;
    }

    @RequestMapping("/addUser")
    public String addUser(){
//        userService.addUser(1);
        return "123";
    }

    @RequestMapping("/updateUser")
    public String updateUser(){
//        userService.updateUser(1);
        return "123";
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(){
//        userService.deleteUser(1);
        return "123";
    }

}