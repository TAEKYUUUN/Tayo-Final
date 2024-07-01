package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatNotice;

public interface ChatNoticeRepository extends JpaRepository<ChatNotice, Long>{
}
