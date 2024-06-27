package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>{

}
