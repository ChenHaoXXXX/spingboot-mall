package com.chenhao.springmall.service.impl;

import com.chenhao.springmall.constant.RoleConstants;
import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.model.MemberHasRole;
import com.chenhao.springmall.dto.MemberRegisterRequest;
import com.chenhao.springmall.model.Role;
import com.chenhao.springmall.repository.MemberHasRoleRepository;
import com.chenhao.springmall.repository.MemberRepository;
import com.chenhao.springmall.repository.RoleRepository;
import com.chenhao.springmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("找不到會員 ID: " + memberId));
        member.setEmail(memberRegisterRequest.getEmail());

        // hash 原始密碼
        String hashedPassword = passwordEncoder.encode(memberRegisterRequest.getPassword());
        member.setPassword(hashedPassword);

        member.setName(memberRegisterRequest.getName());
        member.setLastModifiedDate(new Date());
        Member memberSave = memberRepository.save(member);
        return memberSave;


    }

    //只有 ADMIN權限 才能執行
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteById(Integer memberId) {
        //檢查是否有該會員
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("找不到會員，ID: " + memberId));

        //檢查該會員是否有ADMIN 權限
        List<MemberHasRole> memberHasRoleList = memberHasRoleRepository.findByMemberId(memberId);
        for (MemberHasRole memberHasRole : memberHasRoleList) {
            Boolean isAdmin = memberHasRole.getRoleId().equals(RoleConstants.ADMIN_NUMBER);
            if(isAdmin){
                throw new IllegalArgumentException("無法刪除具有 ADMIN 權限的帳號");
            }
        }

        memberRepository.deleteById(memberId);
        memberHasRoleRepository.deleteByMemberId(memberId);
    }
}
