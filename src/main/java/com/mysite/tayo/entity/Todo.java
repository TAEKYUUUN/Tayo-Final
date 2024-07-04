package com.mysite.tayo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "todo")
public class Todo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq")
	@SequenceGenerator(name = "todo_seq", sequenceName = "TODO_SEQ", allocationSize = 1)
	@Column(name = "todo_idx")
	private Long todoIdx;
	
	@OneToOne
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
    private Post post;
	
	@Column(name = "title", length = 100)
	private String title;
	
	@Column(name = "is_ended")
	private Integer isEnded;
}
