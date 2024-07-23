package com.mysite.tayo.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "chat_unreader")
public class ChatUnreader {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_unreader_seq")
	@SequenceGenerator(name = "chat_unreader_seq", sequenceName = "CHAT_unreader_SEQ", allocationSize = 1)
	@Column(name = "chat_unreader_idx")
	private Long chatUnreaderIdx;
	
	@Column(name = "unreader_idx")
	private Integer unreaderIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_contents_idx")
	private ChatContents chatContents;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx")
	private Member member;
	
	
	
}
