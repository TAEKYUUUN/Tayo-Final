package com.mysite.tayo.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat_contents")
public class ChatContents {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_contents_seq")
	@SequenceGenerator(name = "chat_contents_seq", sequenceName = "CHAT_CONTENTS_SEQ", allocationSize = 1)
	@Column(name = "chat_contents_idx")
	private Long chat_contents_idx;
	
	@Lob
	@Column(name = "text", columnDefinition = "CLOB")
	private String text;
	
	@Column(name = "has_file", length = 500)
	private String hasFile;
	
	@Column(name = "emote", length = 100)
	private String emote;
	
	@Column(name = "time")
	private Timestamp time;
	
	@Column(name = "notice")
	private Integer notice;
	
	@Column(name = "is_reply")
	private Integer isReply;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_idx")
	private Chat chat;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx")
	private Member member;
	
	@OneToMany(mappedBy = "chatContents", cascade = CascadeType.REMOVE)
	private List<ChatUnreader> chatUnreaderList;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
