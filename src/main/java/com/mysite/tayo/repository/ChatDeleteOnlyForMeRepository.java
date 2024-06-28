package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ChatDeleteOnlyForMe;

@Repository
public interface ChatDeleteOnlyForMeRepository extends JpaRepository<ChatDeleteOnlyForMe, Long>{

}
