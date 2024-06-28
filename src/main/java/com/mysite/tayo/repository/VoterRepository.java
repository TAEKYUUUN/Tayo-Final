package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Voter;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long>{

}
