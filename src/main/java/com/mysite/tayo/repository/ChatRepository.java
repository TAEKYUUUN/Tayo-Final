package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	List<Chat> findByChatMemberListMemberMemberIdx(Long memberIdx);
}
