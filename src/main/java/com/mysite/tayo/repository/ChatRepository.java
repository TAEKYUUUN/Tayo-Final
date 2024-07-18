package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{
	List<Chat> findByChatMemberListMemberMemberIdx(Long memberIdx);
	
	
}
