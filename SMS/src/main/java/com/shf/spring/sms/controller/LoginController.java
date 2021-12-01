package com.shf.spring.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shf.spring.sms.entity.User;
import com.shf.spring.sms.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession httpSession, Model model){
        log.info(String.valueOf(user));
//        验证码是否匹配
        if (user.getCaptcha().equalsIgnoreCase((String) httpSession.getAttribute("SESSION_KEY_IMAGE_CODE"))) {
//            用户是否存在
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("username", user.getUsername());
            User u1 = userMapper.selectOne(userQueryWrapper);
            log.info(String.valueOf(u1));
            if (u1 != null){
                // 用户存在 判断密码是否一致
                if (u1.getPassword().equals(user.getPassword())){
                    model.addAttribute("info","登录成功！！！");
                    return "redirect:index";
                } else {
                    model.addAttribute("info","用户名或密码错误，请重新输入！！！");
                    return "login";
                }
            } else {
//                用户不存在
                model.addAttribute("info","用户名或密码错误，请重新输入！！！");
                return "login";
            }
        } else {
            model.addAttribute("info","验证码错误，请重新输入！！！");
            return "login";
        }
    }
}
