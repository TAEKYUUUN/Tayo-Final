package com.mysite.tayo.chat;

import com.mysite.tayo.member.Member;

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
@Table(name = "chat_member")
public class ChatMember {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_member_seq")
	@SequenceGenerator(name = "chat_member_seq", sequenceName = "CHAT_MEMBER_SEQ", allocationSize = 1)
	@Column(name = "chat_member_idx")
	private Long chat_member_idx;	
	
	@Column(name = "leader")
	private Integer leader;
	
	@Column(name = "manager")
	private Integer manager;
	
	@Column(name = "chat_name", length = 100)
	private String chatName;
	
	@Column(name = "peg")
	private Integer peg;
	
	@Column(name = "alarm")
	private Integer alarm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_idx")
	private Chat chat;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx")
	private Member member;
}

