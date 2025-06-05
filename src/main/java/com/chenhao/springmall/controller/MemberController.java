package com.chenhao.springmall.controller;

import com.chenhao.springmall.model.Member;
import com.chenhao.springmall.model.MemberRegisterRequest;
import com.chenhao.springmall.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {
        Integer userId = memberService.register(memberRegisterRequest);
        Member member =  memberService.getMemberById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }


    @GetMapping("/members/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable Integer memberId){
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<Member> modifyUserAccount(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest,
                                                  @PathVariable Integer memberId) {
        Member member = memberService.modifyMemberAccount(memberId,memberRegisterRequest);
        if(member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(member);
        }

    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> deleteByUserId(@PathVariable @Valid Integer memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("刪除成功");
    }

}
