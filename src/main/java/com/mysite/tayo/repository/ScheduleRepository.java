package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{

}
