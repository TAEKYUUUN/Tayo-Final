package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatMember;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long>{
	
}
