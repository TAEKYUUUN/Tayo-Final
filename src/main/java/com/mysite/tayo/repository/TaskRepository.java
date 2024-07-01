package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
