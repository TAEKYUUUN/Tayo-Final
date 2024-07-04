package com.mysite.tayo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
	 @Query("SELECT us FROM UserSession us WHERE us.SessionIdx = (SELECT MAX(u.SessionIdx) FROM UserSession u WHERE u.member.memberIdx = :memberIdx)")
	 Optional<UserSession> findTopByMemberMemberIdxOrderBySessionIdxDesc(@Param("memberIdx") Long memberIdx);
	 
	 @Query("SELECT us FROM UserSession us WHERE us.SessionIdx = (SELECT MAX(u.SessionIdx) FROM UserSession u WHERE u.member.memberIdx = :memberIdx AND u.deviceType = :deviceType)")
	 Optional<UserSession> findTopByMemberMemberIdxAndDeviceTypeOrderBySessionIdxDesc(@Param("memberIdx") Long memberIdx, @Param("deviceType") String deviceType);
}
