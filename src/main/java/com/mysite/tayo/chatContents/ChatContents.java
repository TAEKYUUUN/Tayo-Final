package com.mysite.tayo.chatContents;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
	
	@Column(name = "chat_idx")
	private Integer chatIdx;
	
	@Lob
	@Column(name = "text", columnDefinition = "CLOB")
	private String text;
	
	@Column(name = "has_file", length = 500)
	private String hasFile;
	
	@Column(name = "emote", length = 100)
	private String emote;
	
	@Column(name = "time")
	private Timestamp time;
	
	@Column(name = "from_idx")
	private Integer fromIdx;
	
	@Column(name = "notice")
	private Integer notice;
	
	@Column(name = "is_reply")
	private Integer isReply;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
