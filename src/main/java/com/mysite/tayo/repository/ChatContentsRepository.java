package com.mysite.tayo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatContents;

@Repository
public interface ChatContentsRepository extends JpaRepository<ChatContents, Long>{
	
//	웹소켓에서 가장 최근에 보내진 채팅내용의IDX 를 뽑기
	@Query
	("SELECT MAX(c.chatContentsIdx) FROM ChatContents c WHERE c.member.memberIdx = :userIdx AND c.chat.chatIdx = :chatIdx")
	Long findMaxChatContentIdxByUserIdx(@Param("userIdx") Long userIdx, @Param("chatIdx") Long chatIdx);
//	답장할 채팅의 채팅내용
	@Query
	("SELECT c.text FROM ChatContents c WHERE c.chatContentsIdx = :replyIdx")
	String findChatContentByReplyIdx(@Param("replyIdx") Long replyIdx);
//	답장할 채팅의 글쓴이 이름
	@Query
	("SELECT c.member.name FROM ChatContents c WHERE c.chatContentsIdx = :replyIdx")
	String findMemberNameByReplyIdx(@Param("replyIdx") Long replyIdx);
//	웹소켓에서 쓸 채팅내용
	@Query
	("SELECT c.text FROM ChatContents c WHERE c.chatContentsIdx = :chatContentIdx")
	String findChatContentByChatContentIdx(@Param("chatContentIdx") Long chatContentIdx);
//	웹소켓에서 쓸 채팅 시간
	@Query
	("SELECT TO_CHAR(c.time, 'AM HH12:MI') FROM ChatContents c WHERE c.chatContentsIdx = :chatContentIdx")
	String findChatContentTimeByChatContentIdx(@Param("chatContentIdx") Long chatContentIdx);
//	가장 최신의 공지사항
	@Query
	("SELECT c.chatContentsIdx FROM ChatContents c WHERE c.time = (SELECT max(cc.time) FROM ChatContents cc WHERE cc.notice = 1) AND c.chat.chatIdx = :chatIdx")
	Long findMaxNoticeChatContentIdxByChatContentIdx(@Param("chatIdx") Long chatIdx);

	@Query
	("SELECT c.chatContentsIdx FROM ChatContents c WHERE c.chat.chatIdx = :chatIdx")
	ArrayList<Long> findChatContentsListIdxByChatIdx(@Param("chatIdx") Long chatIdx);
	
}
