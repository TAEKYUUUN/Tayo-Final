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
@Table(name = "post_member")
public class PostMember {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_member_seq")
    @SequenceGenerator(name = "post_member_seq", sequenceName = "POST_MEMBER_SEQ", allocationSize = 1)
    @Column(name = "post_member_idx")
    private Long postMemberIdx;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
    private Member member;
	
	@Column(name = "alarm")
	private Integer alarm;
	
	@Column(name = "bookmark")
	private Integer bookmark;
	
	@Column(name = "re_alarm")
	private Date reAlarm;
	
	@Column(name = "read_time")
	private Date readTime;
}
