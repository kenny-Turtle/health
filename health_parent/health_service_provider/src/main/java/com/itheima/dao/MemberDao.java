package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {

    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberCountByDate(String today);

    Integer findTotalMemberCount();

    Integer findMemberCountAfterDate(String date);

}
