package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.LowerTask;

@Repository
public interface LowerTaskRepository extends JpaRepository<LowerTask, Long>{
}
