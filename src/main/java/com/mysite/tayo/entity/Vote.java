package com.mysite.tayo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vote")
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_seq")
	@SequenceGenerator(name = "vote_seq", sequenceName = "VOTE_SEQ", allocationSize = 1)
	@Column(name = "vote_idx")
	private Long voteIdx;
	
	@OneToOne
    @JoinColumn(name = "post_idx", referencedColumnName = "post_idx")
    private Post post;
	
	@Column(name = "title", length = 100)
	private String title;
	
	@Column(name = "vote_detail", length = 300)
	private String voteDetail;
	
	@Column(name = "vote_end_date")
	private Date voteEndDate;
	
	@Column(name = "end_vote")
	private Integer endVote;
	
	@Column(name = "is_plural")
	private Integer isPlural;
	
	@Column(name = "is_anonymous")
	private Integer isAnonymous;
	
	@OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    private List<VoteItem> voteItems = new ArrayList<>();
}
