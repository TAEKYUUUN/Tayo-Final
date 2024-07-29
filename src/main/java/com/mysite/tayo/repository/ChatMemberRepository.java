package com.mysite.tayo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatMember;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long>{
	
	@Query
	("SELECT cm.member.memberIdx FROM ChatMember cm WHERE cm.chat.chatIdx = :chatIdx AND cm.member.memberIdx != :memberIdx")
	ArrayList<Long> findMemberByChatIdx(@Param("chatIdx") Long chatIdx, @Param("memberIdx") Long memberIdx);

	@Query
	("SELECT cm.chatMemberIdx FROM ChatMember cm WHERE cm.chat.chatIdx = :chatIdx AND cm.member.memberIdx = :memberIdx")
	Long findChatMemberIdxByChatIdxAndMemberIdx(@Param("chatIdx") Long chatIdx, @Param("memberIdx") Long memberIdx);
}
