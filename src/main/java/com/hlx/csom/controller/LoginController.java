package com.hlx.csom.controller;

import com.hlx.csom.base.ResultInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/19 14:56
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    public ResultInfo login(@RequestBody Map<String,String> map) {
        String username=null;
        String password=null;

        if(map.containsKey("username")){
            username=map.get("username");
        }
        if(map.containsKey("password")){
            password=map.get("password");
        }
        if("admin".equals(username)&&
        "123456".equals(password)){
            return new ResultInfo(200,"登录成功");
        }else {
            return new ResultInfo(400,"用户名或密码错误");
        }
    }

}
