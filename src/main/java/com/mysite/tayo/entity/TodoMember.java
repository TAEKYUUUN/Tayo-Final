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
@Table(name = "todo_member")
public class TodoMember {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_member_seq")
	@SequenceGenerator(name = "todo_member_seq", sequenceName = "TODO_MEMBER_SEQ", allocationSize = 1)
	@Column(name = "todo_member_idx")
	private Long todoMemberIdx;
	
	@ManyToOne
	@JoinColumn(name = "todo_name_idx", referencedColumnName = "todo_name_idx")
	private TodoName todoName;
	
	@ManyToOne
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
	
	@Column(name = "is_done")
	private Integer isDone;
	
	public Integer getIsDone() {
        return isDone != null ? isDone : 0; // 기본값 설정
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }
}

