package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.PostMember;

public interface PostMemberRepository extends JpaRepository<PostMember, Long>{

}
