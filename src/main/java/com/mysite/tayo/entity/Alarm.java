package com.mysite.tayo.entity;

import java.util.Date;

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
    @JoinColumn(name = "from_post_idx", referencedColumnName = "post_idx")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_comments_idx", referencedColumnName = "comments_idx")
	private Comments comments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_project_idx", referencedColumnName = "project_idx")
	private Project project;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_post_react_idx", referencedColumnName = "post_react_idx")
	private PostReact postReact;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_comments_react_idx", referencedColumnName = "comments_react_idx")
	private CommentsReact commentsReact;
	
	@Column(name = "is_read")
	private Integer isRead = 1;
	
	@Column(name = "alarm_time")
	private Date alarmTime;
}
