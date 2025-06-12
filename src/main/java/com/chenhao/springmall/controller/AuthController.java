package com.chenhao.springmall.controller;

import com.chenhao.springmall.dto.LoginRequest;
import com.chenhao.springmall.jwt.JwtUtil;
import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.model.Role;
import com.chenhao.springmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            Member member = memberService.getMemberByEmail(loginRequest.getEmail());
            List<String> roles = memberService.getRolesByMemberId(member.getMemberId())
                    .stream().map(Role::getRoleName).toList();

            String token = jwtUtil.generateToken(member.getEmail(), roles);

            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("伺服器錯誤: " + e.getMessage());
        }
    }



    @GetMapping("/hello")
    public String hello() {
        return "JWT OK!";
    }
}