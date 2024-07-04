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
@Table(name = "chat_notice")
public class ChatNotice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_seq")
	@SequenceGenerator(name = "notice_seq", sequenceName = "NOTICE_SEQ", allocationSize = 1)
	@Column(name = "notice_idx")
	private Long noticeIdx;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "chat_contents_idx")
//	private ChatContents chatContents;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "chat")
//	private Chat chat;
//	
//	@Column(name = "notice_regist_date")
//	private Date noticeRegistDate;
	
	
	
}
