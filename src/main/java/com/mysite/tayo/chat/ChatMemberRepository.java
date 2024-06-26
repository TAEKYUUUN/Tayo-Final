package com.mysite.tayo.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long>{
	
}
