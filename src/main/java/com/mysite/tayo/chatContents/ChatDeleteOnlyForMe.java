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
@Table(name = "chat_delete_only_for_me")
public class ChatDeleteOnlyForMe {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_delete_only_for_me_seq")
	@SequenceGenerator(name = "chat_delete_only_for_me_seq", sequenceName = "CHAT_DELETE_ONLY_FOR_ME_SEQ", allocationSize = 1)
	@Column(name = "chat_delete_only_for_me_idx")
	private Long chatDeleteOnlyForMeIdx;
	
	@Column(name = "chat_contents_idx")
	private Integer chatContentsIdx;
	
	@Column(name = "member_idx")
	private Integer memgerIdx;
	
	
	
}
