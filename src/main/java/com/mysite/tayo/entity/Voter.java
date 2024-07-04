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
@Table(name = "voter")
public class Voter {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voter_seq")
	@SequenceGenerator(name = "voter_seq", sequenceName = "VOTER_SEQ", allocationSize = 1)
	@Column(name = "voter_idx")
	private Long voterIdx;
	
	@ManyToOne
	@JoinColumn(name = "member_idx", referencedColumnName = "member_idx")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "vote_item_idx", referencedColumnName = "vote_item_idx")
	private VoteItem voteItem;
	
	@ManyToOne
	@JoinColumn(name = "vote_idx", referencedColumnName = "vote_idx")
	private Vote vote;
}
