package com.mysite.tayo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "User_Session") // 데이터베이스 테이블 이름
public class UserSession {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_seq")
	 @SequenceGenerator(name = "session_seq", sequenceName = "SESSION_SEQ", allocationSize = 1)
	 private Long SessionIdx;
	 
	 @Column(name = "device_type", length=100)
	 private String deviceType;
	 
	 @Column(name = "device_name", length=100)
	 private String deviceName;
	 
	 @Column(name = "login_time", length=100)
	 private LocalDateTime loginTime;
	 
	 @Column(name = "logout_time", length=100)
	 private LocalDateTime logoutTime;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "member_idx")
	 private Member member;
	 
	 @Column(name = "access_ip", length=100)
	 private String accessIp;
}

