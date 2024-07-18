package com.mysite.tayo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.ChatUnreader;
import com.mysite.tayo.entity.Member;

@Repository
public interface ChatUnreaderRepository extends JpaRepository<ChatUnreader, Long>{
	 Optional<ChatUnreader> findByChatContentsAndMember(Optional<ChatContents> chatContents, Optional<Member> member);
	 
	 void deleteAllByChatContentsAndMember(Optional<ChatContents> chatContents, Member member);
	 
//	 채팅내용 마다 안읽은 사람 수
	 @Query
	 ("SELECT COUNT(*) FROM ChatUnreader cu WHERE cu.chatContents.chatContentsIdx = :chatContentsIdx")
	 Integer findChatUnreaderCountByChatContentsIdx(@Param("chatContentsIdx") Long chatContentsIdx);
	 
}
