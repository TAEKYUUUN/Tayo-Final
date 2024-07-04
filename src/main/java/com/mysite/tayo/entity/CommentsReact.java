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
@Table(name = "comments_react")
public class CommentsReact {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_react_seq")
    @SequenceGenerator(name = "comments_react_seq", sequenceName = "COMMENTS_REACT_SEQ", allocationSize = 1)
    @Column(name = "comments_react_idx")
    private Long commentsReactIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comments_idx", referencedColumnName = "comments_idx")
	private Comments comments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
	
	@Column(name = "react")
	private Integer react;
	
	@Column(name = "react_time")
	private Date reactTime;
}
