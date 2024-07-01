package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatMention;

@Repository
public interface ChatMentionRepository extends JpaRepository<ChatMention, Long>{

}
