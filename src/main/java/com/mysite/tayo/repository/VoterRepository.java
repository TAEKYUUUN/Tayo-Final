package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Voter;

public interface VoterRepository extends JpaRepository<Voter, Long>{

}
