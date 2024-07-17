package com.mysite.tayo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comments {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    @SequenceGenerator(name = "comments_seq", sequenceName = "COMMENTS_SEQ", allocationSize = 1)
    @Column(name = "comments_idx")
    private Long commentsIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
	@JsonBackReference
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
    private Member member;
	
	@Column(name = "write_time")
	private Date writeTime;
	
	@Column(name = "contents")
	private String contents;
	
	@Column(name = "has_file")
	private String hasFile;
	
	@Transient
    private boolean isLiked;
	
	@OneToMany(mappedBy = "comments", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UncheckComments> uncheckComments = new ArrayList<>();
	
	@OneToMany(mappedBy = "comments", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentsReact> commentsReacts = new ArrayList<>();
	
	@OneToMany(mappedBy = "comments", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Alarm> alarms = new ArrayList<>();
}
