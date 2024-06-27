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
@Table
public class ChatMention {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_mention_seq")
	@SequenceGenerator(name = "chat_mention_seq", sequenceName = "CHAT_MENTION_SEQ", allocationSize = 1)
	@Column(name = "chat_mention_idx")
	private Long chatMentionIdx;

	@Column(name = "chat_contents_idx")
	private Integer chatContentsIdx;
	
	@Column(name = "mentioned_idx")
	private Integer mentionedIdx;
	
}
