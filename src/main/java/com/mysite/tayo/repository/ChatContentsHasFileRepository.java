package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatContentsHasFile;

public interface ChatContentsHasFileRepository extends JpaRepository<ChatContentsHasFile, Long>{

}
