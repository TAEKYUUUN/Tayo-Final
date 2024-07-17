package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.LowerTask;
import com.mysite.tayo.entity.Task;

import jakarta.transaction.Transactional;

@Repository
public interface LowerTaskRepository extends JpaRepository<LowerTask, Long>{
	
	@Transactional
    @Modifying
    @Query("DELETE FROM LowerTask lt WHERE lt.task = :task")
    void deleteAllByTask(@Param("task") Task task);
}
