package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	// postIdx 를 갖고 해당하는 Vote 가져오기
	Optional<Vote> findByPostPostIdx(Long postIdx);
}
