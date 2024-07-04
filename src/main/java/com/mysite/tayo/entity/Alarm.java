package com.mysite.tayo.entity;

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
@Table(name = "alarm")
public class Alarm {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alarm_seq")
    @SequenceGenerator(name = "alarm_seq", sequenceName = "ALARM_SEQ", allocationSize = 1)
    @Column(name = "alarm_idx")
    private Long alarmIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
    private Member member;
	
	@Column(name = "alarm_type")
	private Integer alarmType;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comments_idx", referencedColumnName = "comments_idx")
	private Comments comments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_idx", referencedColumnName = "project_idx")
	private Project project;
}
