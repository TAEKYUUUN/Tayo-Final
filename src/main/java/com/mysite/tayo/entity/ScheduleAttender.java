package com.mysite.tayo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "schedule_attender")
public class ScheduleAttender {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_attender_seq")
	@SequenceGenerator(name = "schedule_attender_seq", sequenceName = "SCHEDULE_ATTENDER_SEQ", allocationSize = 1)
	@Column(name = "schedule_attender_idx")
	private Long scheduleAttenderIdx;
	
	@ManyToOne
	@JoinColumn(name = "schedule_idx", referencedColumnName = "schedule_idx")
	private Schedule schedule;
	
	@ManyToOne
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
	
	@Column(name = "is_attend")
	private Integer isAttend;
}
