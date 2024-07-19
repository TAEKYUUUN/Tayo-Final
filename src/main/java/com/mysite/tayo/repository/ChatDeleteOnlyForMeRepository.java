package com.mysite.tayo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatDeleteOnlyForMe;
import com.mysite.tayo.entity.Member;

@Repository
public interface ChatDeleteOnlyForMeRepository extends JpaRepository<ChatDeleteOnlyForMe, Long>{
	
	@Query("SELECT c.chatContents.chatContentsIdx FROM ChatDeleteOnlyForMe c " +
	           "WHERE c.member = :member")
	    ArrayList<Long> findChatContentsIdxByMember(@Param("member") Member member);
	
}
