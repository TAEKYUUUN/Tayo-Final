package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatMention;

public interface ChatMentionRepository extends JpaRepository<ChatMention, Long>{

}
