package com.mysite.tayo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/chatRoom/{chatIdx}")
	public String chat(Model model, @PathVariable("chatIdx") Long chatIdx) {
		Optional<Chat> _chatList = this.chatService.getId(chatIdx);
		Chat chatList = _chatList.get();
		model.addAttribute("chatList", chatList);
		return "chatRoom";
	}

	@PostMapping("/chatRoom/{chatIdx}")
	public String chatAdd(@RequestParam(value = "chatContents", required = false) String chatContents,
	                      @RequestParam(value = "replyIdx", required = false) Long replyIdx,
	                      @RequestParam(value = "chatContentsIdx", required = false) Long chatContentsIdx,
	                      @PathVariable("chatIdx") Long chatIdx,
	                      Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);
	   
	    if (chatContentsIdx == null) {
	    	chatService.addChatContent(chatIdx, chatContents, member, replyIdx);
	    } else {
	    	chatService.addNotice(chatContentsIdx, member);
	    }
	    
	    
	    return "redirect:/chatRoom/" + chatIdx;
	}

	
	
	
	
	
	@GetMapping("/chat")
	public String chatRoom(Model model) {
		List<Chat> chatList = this.chatService.getList();
		model.addAttribute("chatList", chatList);
		return "chat";
	}

	@GetMapping("/chatCollection")
	public String chatCollection(Model model) {
		List<Chat> chatList = this.chatService.getList();
		model.addAttribute("chatList", chatList);
		return "chatCollection";
	}

	@GetMapping("/chatNoticeText.html")
	public String chatNotice(Model model) {
		return "chatNoticeText";
	}
}
