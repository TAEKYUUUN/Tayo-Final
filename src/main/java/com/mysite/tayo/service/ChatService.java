package com.mysite.tayo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	
	private final ChatRepository chatRepository;
	
	public List<Chat> getList() {
		return this.chatRepository.findByChatMemberListMemberMemberIdx(1L);
	}

	public Optional<Chat> getId() {
		return this.chatRepository.findById(1L);
	}
}
