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
@Table(name = "uncheck_comments_react")
public class UncheckCommentsReact {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uncheck_comments_react_seq")
    @SequenceGenerator(name = "uncheck_comments_react_seq", sequenceName = "UNCHECK_COMMENTS_REACT_SEQ", allocationSize = 1)
    @Column(name = "uncheck_comments_react_idx")
    private Long uncheckCommentsReactIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comments_react_idx", referencedColumnName = "comments_react_idx")
	private CommentsReact commentsReact;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
}
