package com.mysite.tayo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_seq")
	@SequenceGenerator(name = "schedule_seq", sequenceName = "SCHEDULE_SEQ", allocationSize = 1)
	@Column(name = "schedule_idx")
	private Long scheduleIdx;
	
	@OneToOne
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
    private Post post;
	
	@Column(name = "title", length = 100)
	private String title;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "place", length = 200)
	private String place;
	
	@Lob
	@Column(name = "contents", columnDefinition = "CLOB")
	private String contents;
	
}
