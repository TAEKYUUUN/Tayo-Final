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
@Table(name = "unread")
public class Unread {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unread_seq")
    @SequenceGenerator(name = "unread_seq", sequenceName = "UNREAD_SEQ", allocationSize = 1)
    @Column(name = "unread_idx")
    private Long unreadIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comments_idx", referencedColumnName = "comments_idx")
	private Comments comments;
}
