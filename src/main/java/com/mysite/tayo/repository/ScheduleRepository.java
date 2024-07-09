package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Paragraph;
import com.mysite.tayo.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
	// postIdx 를 갖고 해당하는 Schedule 가져오기
	Optional<Schedule> findByPostPostIdx(Long postIdx);
}
