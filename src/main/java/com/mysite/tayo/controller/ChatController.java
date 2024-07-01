package com.mysite.tayo.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.ChatContentsRepository;
import com.mysite.tayo.repository.ChatRepository;
import com.mysite.tayo.service.ChatService;
import com.mysite.tayo.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	
	private final ChatService chatService;
	private final MemberService memberService;
	private final ChatRepository chatRepository;
	private final ChatContentsRepository chatContentsRepository;
	
	@GetMapping("/chatRoom")
	public String chat(Model model) {
		Optional<Chat> _chatList = this.chatService.getId();
		Chat chatList = _chatList.get();
		model.addAttribute("chatList", chatList);
		return "chatRoom";
	}
	
	@PostMapping("/chatRoom")
	public String chatAdd(@RequestParam("chatContents") String chatContents
							, @RequestParam("chatIdx") Long chatIdx
							, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Optional<Chat> _chat = chatRepository.findById(chatIdx);
		ChatContents chatContent = new ChatContents();
		chatContent.setChat(_chat.get());
		chatContent.setText(chatContents);
		Date date = new Date();
		chatContent.setTime((Timestamp)date);
		chatContent.setMember(member);
		chatContentsRepository.save(chatContent);
		return "redirect:/chatRoom";
	}
	
	
	@GetMapping("/chat")
	public String chatRoom(Model model) {
		List<Chat> chatList = this.chatService.getList();
		model.addAttribute("chatList", chatList);	
		return "chat";
	}
	
	
}
