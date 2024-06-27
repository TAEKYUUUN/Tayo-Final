package com.mysite.tayo.company;


import java.util.Date;

import org.checkerframework.common.aliasing.qual.Unique;

import com.mysite.tayo.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
	@SequenceGenerator(name = "company_seq", sequenceName = "COMPANY_SEQ", allocationSize = 1)
	@Column(name = "company_idx")
	private Long companyIdx;

	@Column(name="company_name", length=100)
	private String companyName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx")
	private Member manager;
	
	
	@Column(name="payment")
	private Integer payment;
	
	@Column(name="expiration_date")
	private Date expirationDate;
	
	@Column(name="LOGO", length=100)
	private String logo;
	
	@Unique
	@Column(name="URL", length=100)
	private String url;
	
	@Column(name="participation_method")
	private Integer participationMethod;
	
	@Column(name="security")
	private Integer security;
	
	@Column(name="capture")
	private Integer cpature;

	@Column(name="watermark", length=100)
	private String watermark;
	
	@Column(name="business_number")
	private Integer businessNumber;
	
}
