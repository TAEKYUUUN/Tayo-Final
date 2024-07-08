package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Member;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{
	
	@Query("SELECT a FROM Alarm a WHERE a.member = :member")
	List<Alarm> findByMemberMemberIdx(@Param("member") Member member, Sort sort);
}
