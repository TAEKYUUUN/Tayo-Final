package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{

}
