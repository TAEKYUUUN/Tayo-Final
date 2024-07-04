package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{

}
