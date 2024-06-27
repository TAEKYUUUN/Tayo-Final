package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ChatDeleteOnlyForMe;

public interface ChatDeleteOnlyForMeRepository extends JpaRepository<ChatDeleteOnlyForMe, Long>{

}
