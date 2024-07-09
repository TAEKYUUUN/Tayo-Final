package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Paragraph;
import com.mysite.tayo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
	// postIdx 를 갖고 해당하는 Task 가져오기
	Optional<Task> findByPostPostIdx(Long postIdx);
}
