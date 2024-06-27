package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
