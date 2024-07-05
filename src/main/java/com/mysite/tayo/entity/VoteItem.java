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
@Table(name = "vote_item")
public class VoteItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_item_seq")
	@SequenceGenerator(name = "vote_item_seq", sequenceName = "VOTE_ITEM_SEQ", allocationSize = 1)
	@Column(name = "vote_item_idx")
	private Long voteItemIdx;
	
	@ManyToOne
	@JoinColumn(name = "vote_idx", referencedColumnName = "vote_idx")
	private Vote vote;
	
	@Column(name = "item_name", length = 100)
	private String itemName;
}
