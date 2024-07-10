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
@Table(name = "lower_task")
public class LowerTask {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lower_task_seq")
    @SequenceGenerator(name = "lower_task_seq", sequenceName = "LOWER_TASK_SEQ", allocationSize = 1)
    @Column(name = "lower_task_idx")
	private Long LowerTaskIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upperTask_idx", referencedColumnName = "task_idx")
	private Task task;
	
	@Column(name = "task_name", length = 100)
	private String taskName;
	
	@Column(name = "condition")
	private Integer condition;

}
