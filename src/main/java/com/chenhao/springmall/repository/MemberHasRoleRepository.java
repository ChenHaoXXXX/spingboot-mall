package com.chenhao.springmall.repository;


import com.chenhao.springmall.model.MemberHasRole;
import org.springframework.data.repository.CrudRepository;


public interface MemberHasRoleRepository extends CrudRepository<MemberHasRole,Integer> {
    void deleteByMemberId(Integer memberId);

}
