package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatContents;

@Repository
public interface ChatContentsRepository extends JpaRepository<ChatContents, Long>{
	
	@Query
	("SELECT MAX(c.chatContentsIdx) FROM ChatContents c WHERE c.member.memberIdx = :userIdx AND c.chat.chatIdx = :chatIdx")
	Long findMaxChatContentIdxByUserIdx(@Param("userIdx") Long userIdx, @Param("chatIdx") Long chatIdx);
	
	@Query
	("SELECT c.text FROM ChatContents c WHERE c.chatContentsIdx = :replyIdx")
	String findChatContentByReplyIdx(@Param("replyIdx") Long replyIdx);
	@Query
	("SELECT c.member.name FROM ChatContents c WHERE c.chatContentsIdx = :replyIdx")
	String findMemberNameByReplyIdx(@Param("replyIdx") Long replyIdx);
	
	@Query
	("SELECT c.text FROM ChatContents c WHERE c.chatContentsIdx = :chatContentIdx")
	String findChatContentByChatContentIdx(@Param("chatContentIdx") Long chatContentIdx);
	@Query
	("SELECT TO_CHAR(c.time, 'AM HH12:MI') FROM ChatContents c WHERE c.chatContentsIdx = :chatContentIdx")
	String findChatContentTimeByChatContentIdx(@Param("chatContentIdx") Long chatContentIdx);
}
