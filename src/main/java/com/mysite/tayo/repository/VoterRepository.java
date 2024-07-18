package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Vote;
import com.mysite.tayo.entity.Voter;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long>{

	List<Voter> findByVoteAndMember(Vote vote, Member member);
}
