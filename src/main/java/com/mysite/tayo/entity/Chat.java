package com.mysite.tayo.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.tayo.TimeAgo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	
//dads
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_idx")
	private Company company;
	
	@Column(name = "project_idx")
	private Integer projectIdx;
	
	@OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE) 
    private List<ChatMember> chatMemberList; 
	
	@OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
	private List<ChatContents> chatContentsList;
	

	@Transient 
	private ChatContents lastChatContents;
 
	@Transient
    private String lastChatTime;
	
    public ChatContents getLastChatContents() {
        if (chatContentsList == null || chatContentsList.isEmpty()) {
            return null;
        }
        return chatContentsList.stream()
                .max(Comparator.comparing(ChatContents::getTime))
                .orElse(null);
    }

    public String getLastChatTime() {
        ChatContents lastContent = getLastChatContents();
        if (lastContent != null) {
            LocalDateTime lastTime = lastContent.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            return TimeAgo.toRelative(lastTime);
        }
        return "없음";
    }

    @Transient
    private int unreadCount;

    public int getUnreadCount(Member member) {
        return (int) chatContentsList.stream()
                .flatMap(chatContents -> chatContents.getChatUnreaderList().stream())
                .filter(chatUnreader -> chatUnreader.getMember() != null && chatUnreader.getMember().equals(member))
                .count();
    }
	
}
