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
@Table(name = "post_react")
public class PostReact {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_react_seq")
    @SequenceGenerator(name = "post_react_seq", sequenceName = "POST_REACT_SEQ", allocationSize = 1)
    @Column(name = "post_react_idx")
    private Long postReactIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
	
	@Column(name = "react")
	private Integer react;
	
	@Column(name = "react_time")
	private Date reactTime;
}
