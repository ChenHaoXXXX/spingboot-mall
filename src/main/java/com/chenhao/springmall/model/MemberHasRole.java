package com.chenhao.springmall.model;

import jakarta.persistence.*;

@Entity
@Table(name = "member_has_role")
public class MemberHasRole {
    @Id
    @Column(name = "member_has_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer memberHasRoleId;

    @Column(name = "member_id")
    Integer memberId;

    @Column(name = "role_id")
    Integer roleId;

    public Integer getMemberHasRoleId() {
        return memberHasRoleId;
    }

    public void setMemberHasRoleId(Integer memberHasRoleId) {
        this.memberHasRoleId = memberHasRoleId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
