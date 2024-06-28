package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.PostMember;

@Repository
public interface PostMemberRepository extends JpaRepository<PostMember, Long>{

}
