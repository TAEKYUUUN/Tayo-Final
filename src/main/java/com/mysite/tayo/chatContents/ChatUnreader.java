package com.mysite.tayo.chatContents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@Column(name = "chat_contents_idx")
	private Integer chatContentsIdx;
	
	@Column(name = "unreader_idx")
	private Integer unreaderIdx;
	
	
}
