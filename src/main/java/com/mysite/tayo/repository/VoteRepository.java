package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{
	// 아무거나 주석
}
