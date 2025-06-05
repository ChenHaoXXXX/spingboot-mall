package com.chenhao.springmall.security;

import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.model.Role;
import com.chenhao.springmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 從資料庫中查詢 Member 數據
        Member member = memberService.getMemberByEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException("Member not found for: " + username);
        } else {
            String memberEmail = member.getEmail();
            String memberPassword = member.getPassword();


            List<Role> roleList = memberService.getRolesByMemberId(member.getMemberId());
            System.out.println(roleList.get(0).getRoleName());

            // 權限部分
            List<GrantedAuthority> authorities = convertToAuthority(roleList);

            // 轉換成 Spring Security 指定的 User 格式
            return new User(memberEmail, memberPassword, authorities);
        }
    }

    public List<GrantedAuthority> convertToAuthority(List<Role> roleList){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(Role role: roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;

    }
}