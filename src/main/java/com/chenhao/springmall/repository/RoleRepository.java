package com.chenhao.springmall.repository;

import com.chenhao.springmall.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role,Integer> {
    @Query(value = "SELECT role.role_id, role.role_name " +
            "FROM role " +
            "JOIN member_has_role ON role.role_id = member_has_role.role_id " +
            "WHERE member_has_role.member_id = :memberId", nativeQuery = true)
    List<Role> findRolesByMemberId(@Param("memberId") Integer memberId);
}
