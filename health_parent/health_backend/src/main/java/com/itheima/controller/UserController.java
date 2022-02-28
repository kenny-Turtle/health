package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jay
 * @date 2022/2/24 9:38 上午
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @RequestMapping("/getUsername")
    public Result getUsername() {
        try {
            //当Spring securty完成认证后，会将当前当用户信息保存到框架提供当上下文对象中
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

    }
}
