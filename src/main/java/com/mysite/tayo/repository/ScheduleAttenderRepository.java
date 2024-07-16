package com.mysite.tayo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ScheduleAttender;

@Repository
public interface ScheduleAttenderRepository extends JpaRepository<ScheduleAttender, Long>{
	
	// 포스트 idx 와 멤버 idx 로 해당하는 scheduleAttender 검색
	Optional<ScheduleAttender> findBySchedulePostPostIdxAndMemberMemberIdx(Long postIdx, Long memberIdx);
	
	// 포스트 idx로 해당하는 scheduleAttender 리스트로 받아오기
	List<ScheduleAttender> findBySchedulePostPostIdx(Long postIdx);
}
