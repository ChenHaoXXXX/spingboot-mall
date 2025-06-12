package com.chenhao.springmall.controller;

import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.dto.MemberRegisterRequest;
import com.chenhao.springmall.model.Product;
import com.chenhao.springmall.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        return "register";
    }

    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> registerApi(@RequestBody @Valid MemberRegisterRequest request) {
        try {
            Integer userId = memberService.register(request);
            return ResponseEntity.ok().body(Map.of("message", "註冊成功", "userId", userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/members/{memberId}")
    public String getMemberById(@PathVariable Integer memberId,Model model){
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "admin";
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<Member> modifyUserAccount(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest,
                                                  @PathVariable Integer memberId) {
        Member member = memberService.modifyMemberAccount(memberId,memberRegisterRequest);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> deleteByUserId(@PathVariable @Valid Integer memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("刪除成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin";
    }

//    public List<Product> getProducts(ProductQueryParams params) {
//
//    }

}
