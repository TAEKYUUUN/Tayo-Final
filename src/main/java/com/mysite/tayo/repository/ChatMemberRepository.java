package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatMember;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long>{
	
}
