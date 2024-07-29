package com.mysite.tayo.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddChatMemberRequest {
    private Long chatIdx;
    private List<Long> members;

    // getters and setters
}