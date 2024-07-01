package com.mysite.tayo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "CHAT_SEQ", allocationSize = 1)
    @Column(name = "chat_idx")
	private Long chatIdx;
	
	@Column(name = "company_idx")
	private Integer companyIdx;
	
	@Column(name = "project_idx")
	private Integer projectIdx;
	
	@OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE) 
    private List<ChatMember> chatMemberList; 
	
	@OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
	private List<ChatContents> chatContentsList;

}
