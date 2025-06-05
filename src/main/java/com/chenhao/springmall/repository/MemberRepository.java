package com.chenhao.springmall.repository;

import com.chenhao.springmall.model.Member;

import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member,Integer> {
    Member findByEmail(String email);
}
