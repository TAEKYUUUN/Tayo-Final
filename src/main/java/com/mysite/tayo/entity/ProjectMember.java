package com.mysite.tayo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "project_member")
public class ProjectMember {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_member_seq")
    @SequenceGenerator(name = "project_member_seq", sequenceName = "PROJECT_MEMBER_SEQ", allocationSize = 1)
    @Column(name = "project_member_idx")
	private Long projectMemberIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_idx")
	@JsonBackReference
	private Project project;
	
	@Column(name = "is_manager")
	private Integer isManager;
	
	@Column(name = "hotlist")
	private Integer hotlist;
	
	@Column(name = "hide")
	private Integer hide;
	
	@Column(name = "alarm")
	private Integer alarm;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", referencedColumnName = "company_idx")
    @JsonIgnore
	private Company company;
	
	@Column(name = "is_confirmed")
	private Integer isConfirmed;
}
