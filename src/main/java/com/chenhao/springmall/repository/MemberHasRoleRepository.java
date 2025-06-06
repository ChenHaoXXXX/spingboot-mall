package com.chenhao.springmall.repository;


import com.chenhao.springmall.model.MemberHasRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MemberHasRoleRepository extends CrudRepository<MemberHasRole,Integer> {
    void deleteByMemberId(Integer memberId);
    List<MemberHasRole> findByMemberId(Integer memberId);

}
