package com.mysite.tayo.entity;

import java.util.Date;

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
@Table(name = "todo_name")
public class TodoName {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_name_seq")
	@SequenceGenerator(name = "todo_name_seq", sequenceName = "TODO_NAME_SEQ", allocationSize = 1)
	@Column(name = "todo_name_idx")
	private Long todoNameIdx;
	
	@ManyToOne
	@JoinColumn(name = "todo_idx", referencedColumnName = "todo_idx")
	private Todo todo;
	
	@Column(name = "todo_name", length = 100)
	private String todoName;
	
	@ManyToOne
	@JoinColumn(name = "todo_manager_idx", referencedColumnName = "member_idx")
	private Member todoManager;
	
	@Column(name = "is_finished")
	private Integer isFinished;
	
	@Column(name = "deadline")
	private Date deadline;
}
