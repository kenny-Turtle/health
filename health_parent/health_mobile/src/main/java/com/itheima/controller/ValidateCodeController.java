package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.ValidateCodeUtils;
import javafx.scene.shape.Mesh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.nio.charset.CoderMalfunctionError;
import java.util.PrimitiveIterator;

/**
 * @author Jay
 * @date 2022/2/23 8:35 上午
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /* 预约体检套餐时
     * 向手机号码发送随机四位验证码
     * */
    @PostMapping("/send4Order")
    public Result send4Order(Long telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送短信
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为："+ code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /*
     * 登录时
     * */
    @RequestMapping("/send4Login")
    public Result send4Login(Long telephone) {
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送短信
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + validateCode);
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 30 * 60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
