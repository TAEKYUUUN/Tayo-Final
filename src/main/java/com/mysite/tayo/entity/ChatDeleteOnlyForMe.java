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
@Table(name = "chat_delete_only_for_me")
public class ChatDeleteOnlyForMe {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_delete_only_for_me_seq")
	@SequenceGenerator(name = "chat_delete_only_for_me_seq", sequenceName = "CHAT_DELETE_ONLY_FOR_ME_SEQ", allocationSize = 1)
	@Column(name = "chat_delete_only_for_me_idx")
	private Long chatDeleteOnlyForMeIdx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_contents_idx")
	private ChatContents chatContents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx") // 수정된 부분
	private Member member;

}