package com.mysite.tayo.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
    private Long chatIdx;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // Getters and setters
}