package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author Jay
 * @date 2022/2/23 1:40 下午
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(HttpServletResponse res, @RequestBody Map map) {
//        1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从redis中获取验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        } else {
//        2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                Member newMember = new Member();
                newMember.setPhoneNumber(telephone);
                newMember.setRegTime(new Date());
                memberService.add(newMember);
            }
            //登陆成功
//        3、向客户端写入Cookie，内容为用户手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath("/");
            res.addCookie(cookie);
//        4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60 * 30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
    }
}
