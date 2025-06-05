package com.chenhao.springmall.service.impl;

import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.model.MemberHasRole;
import com.chenhao.springmall.model.MemberRegisterRequest;
import com.chenhao.springmall.model.Role;
import com.chenhao.springmall.repository.MemberHasRoleRepository;
import com.chenhao.springmall.repository.MemberRepository;
import com.chenhao.springmall.repository.RoleRepository;
import com.chenhao.springmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberHasRoleRepository memberHasRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @Override
    public Integer register(MemberRegisterRequest memberRegisterRequest) {
        // 省略參數檢查 (ex: email 是否被註冊過)

        // hash 原始密碼
        String hashedPassword = passwordEncoder.encode(memberRegisterRequest.getPassword());
        memberRegisterRequest.setPassword(hashedPassword);

        // 在資料庫中插入 Member 數據
        Member member = new Member();
        member.setEmail(memberRegisterRequest.getEmail());
        member.setPassword(memberRegisterRequest.getPassword());

        Date now = new Date();
        member.setCreatedDate(now);
        member.setLastModifiedDate(now);

        if(memberRegisterRequest.getName().isBlank()){
            member.setName(memberRegisterRequest.getEmail());
        } else {
            member.setName(memberRegisterRequest.getName());
        }

        Integer memberId = memberRepository.save(member).getMemberId();

        //在資料庫中插入 Member Role 會員權限
        MemberHasRole memberHasRole = new MemberHasRole();
        memberHasRole.setMemberId(memberId);
        memberHasRole.setRoleId(2);
        memberHasRoleRepository.save(memberHasRole);

        return memberId;
    }

    @Override
    public Member getMemberById(Integer id) {
        Member member = memberRepository.findById(id).orElse(null);
        return member;
    }

    @Override
    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        return member;
    }

    @Override
    public List<Role> getRolesByMemberId(Integer memberId) {
        List<Role> roleList = roleRepository.findRolesByMemberId(memberId);
        return roleList;
    }

    @Override
    public Member modifyMemberAccount(Integer memberId,MemberRegisterRequest memberRegisterRequest) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null) {
            return null;
        }else {
            member.setEmail(memberRegisterRequest.getEmail());

            // hash 原始密碼
            String hashedPassword = passwordEncoder.encode(memberRegisterRequest.getPassword());
            member.setPassword(hashedPassword);

            member.setName(memberRegisterRequest.getName());
            member.setLastModifiedDate(new Date());
            Member memberSave = memberRepository.save(member);
            return memberSave;
        }

    }

    @Override
    public void deleteById(Integer memberId) {
        memberRepository.deleteById(memberId);
        memberHasRoleRepository.deleteByMemberId(memberId);
        //memberHasRoleRepository.deleteById();
    }
}
