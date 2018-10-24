package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map name(){
        //获取当前登录人名称
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map loginMap = new HashMap<>();
        loginMap.put("loginName", name);
        return loginMap;
    }
}
