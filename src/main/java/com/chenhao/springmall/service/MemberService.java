package com.chenhao.springmall.service;

import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.dto.MemberRegisterRequest;
import com.chenhao.springmall.model.Role;


import java.util.List;


public interface MemberService {
    Integer register(MemberRegisterRequest memberRegisterRequest);
    Member getMemberById(Integer id);
    Member getMemberByEmail(String email);
    List<Role> getRolesByMemberId(Integer memberId);
    Member modifyMemberAccount(Integer memberId,MemberRegisterRequest memberRegisterRequest);
    void deleteById(Integer memberId);
}
