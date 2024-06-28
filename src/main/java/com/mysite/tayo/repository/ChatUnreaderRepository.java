package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatUnreader;

@Repository
public interface ChatUnreaderRepository extends JpaRepository<ChatUnreader, Long>{

}
