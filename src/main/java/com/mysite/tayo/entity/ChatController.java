package com.mysite.tayo.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.tayo.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	
	private final ChatService chatService;
	
	
	@GetMapping("/chatRoom")
	public String chat(Model model) {
		Optional<Chat> _chatList = this.chatService.getId();
		Chat chatList = _chatList.get();
		model.addAttribute("chatList", chatList);
		return "chatRoom";
	}
	
	@GetMapping("/chat")
	public String chatRoom(Model model) {
		List<Chat> chatList = this.chatService.getList();
		model.addAttribute("chatList", chatList);	
		return "chat";
	}
	
	
}
