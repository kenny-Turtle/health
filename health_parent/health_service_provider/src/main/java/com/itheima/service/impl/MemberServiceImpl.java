package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay
 * @date 2022/2/23 1:55 下午
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    public void add(Member member) {
        if (member.getPassword() != null) {
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    public List<Integer> findMemberCountByMonth(List<String> months) {
        //由于传过来的日期只有年月，所以需要补上日期
        List<Integer> list = new ArrayList<Integer>();
        for (String month : months) {
            String dateEnd = DateUtils.getDateEnd(month,"\\.");
//            month = month + ".31";
            Integer count = memberDao.findMemberCountBeforeDate(dateEnd);
            list.add(count);
        }
        return list;
    }
}
