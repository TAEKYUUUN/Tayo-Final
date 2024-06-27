package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatContents;

public interface ChatContentsRepository extends JpaRepository<ChatContents, Long>{
}
