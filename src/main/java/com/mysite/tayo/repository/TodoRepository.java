package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Schedule;
import com.mysite.tayo.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	// postIdx 를 갖고 해당하는 Todo 가져오기
	Optional<Todo> findByPostPostIdx(Long postIdx);
}
