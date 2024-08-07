package com.mysite.tayo.entity;

import java.util.Date;
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
import jakarta.persistence.OneToOne;
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
    private Long chatContentsIdx;

    @Lob
    @Column(name = "text", columnDefinition = "CLOB")
    private String text;

    @Column(name = "link")
    private boolean link = false;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "has_file_idx", referencedColumnName = "has_file_idx")
    private ChatContentsHasFile hasFile;

    @Column(name = "emote", length = 100)
    private String emote;

    @Column(name = "time")
    private Date time;

    @Column(name = "notice", nullable = true)
    private Integer notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_idx")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    @OneToMany(mappedBy = "chatContents", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatUnreader> chatUnreaderList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "is_reply")
    private ChatContents isReply;

    @OneToMany(mappedBy = "chatContents", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatDeleteOnlyForMe> chatDeleteOnlyForMeList;
    
    public String getFileUrl() {
        return this.hasFile != null ? "/uploads/" + this.hasFile.getFileName() : null;
    }

    public boolean isImage() {
        return this.hasFile != null && (this.hasFile.getFileType().equals("image/png") || this.hasFile.getFileType().equals("image/jpeg"));
    }

    
}