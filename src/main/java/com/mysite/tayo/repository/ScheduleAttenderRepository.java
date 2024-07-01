package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ScheduleAttender;

@Repository
public interface ScheduleAttenderRepository extends JpaRepository<ScheduleAttender, Long>{

}
