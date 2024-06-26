package com.mysite.tayo.chat;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final ChatRepository chatRepository;
	
	
	@GetMapping("/chat")
	public String chat(Model model) {
		return "chat";
	}
	
	@GetMapping("/chatRoom")
	public String chatRoom(Model model) {
		List<Chat> chatList = this.chatRepository.findByChatMemberListMemberMemberIdx(1L);
		model.addAttribute("chatList", chatList);	
		return "chatRoom";
	}
	
	
}
