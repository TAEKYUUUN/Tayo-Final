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
@Table(name = "uncheck_comments")
public class UncheckComments {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uncheck_comments_seq")
    @SequenceGenerator(name = "uncheck_comments_seq", sequenceName = "UNCHECK_COMMENTS_SEQ", allocationSize = 1)
    @Column(name = "uncheck_comments_idx")
    private Long uncheckCommentsIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comments_idx", referencedColumnName = "comments_idx")
	private Comments comments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
}
