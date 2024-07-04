package com.mysite.tayo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "TASK_SEQ", allocationSize = 1)
    @Column(name = "task_idx")
	private Long taskIdx;
	
	@OneToOne
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
    private Post post;
	
	@Column(name = "task_name", length = 100)
	private String taskName;
	
	@Column(name = "condition")
	private Integer condition;
	
	@Column(name = "task_priority")
	private Integer taskPriority;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_idx", referencedColumnName = "member_idx")
    private Member member;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "upload_date")
	private Date uploadDate;
	
	@Lob
	@Column(name = "contents", columnDefinition = "CLOB")
	private String contents;
}
