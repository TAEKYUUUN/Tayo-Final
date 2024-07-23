package com.mysite.tayo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.service.ChatService;
import com.mysite.tayo.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Controller
@Log4j2
public class ChatController {

	private final ChatService chatService;
	private final MemberService memberService;

	
	@GetMapping("/chatRoom/{chatIdx}")
	public String chat(Model model, @PathVariable("chatIdx") Long chatIdx, Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);
	    Optional<Chat> _chatList = this.chatService.getId(chatIdx);
	    Chat chatList = _chatList.get();

	    // 채팅 내용을 시간 기준으로 정렬합니다.
	    List<ChatContents> sortedChatContentsList = chatList.getChatContentsList().stream()
	        .sorted(Comparator.comparing(ChatContents::getTime))
	        .collect(Collectors.toList());
	    
	    Long maxNotice = chatService.maxNotice(chatIdx);
	    ArrayList<Long> chatDeleteOnlyForMeArr = chatService.chatDeleteOnlyForMeChatContentIdx(member);
	    List<Long> chatDeleteOnlyForMeList = chatDeleteOnlyForMeArr;
	    System.out.println(chatDeleteOnlyForMeList);
	    
	    for(int i=0; i<chatDeleteOnlyForMeList.size(); i++) {
	    	for(int j=0; j<sortedChatContentsList.size(); j++) {
	    		if(chatDeleteOnlyForMeList.get(i) == sortedChatContentsList.get(j).getChatContentsIdx()) {
	    			sortedChatContentsList.remove(j);
	    		}
	    	}
	    }
	    
	    model.addAttribute("member", member);
	    model.addAttribute("chatList", chatList);
	    model.addAttribute("sortedChatContentsList", sortedChatContentsList);
	    model.addAttribute("maxNotice", maxNotice);
	    model.addAttribute("chatDeleteOnlyForMeList", chatDeleteOnlyForMeList);
	    return "chatRoom";
	}

    @PostMapping("/chatRoom/{chatIdx}")
    public String chatAdd(@RequestParam(value = "chatContents", required = false) String chatContents,
                          @RequestParam(value = "replyIdx", required = false) Long replyIdx,
                          @RequestParam(value = "chatContentsIdx", required = false) Long chatContentsIdx,
                          @RequestParam(value = "file", required = false) MultipartFile file,
                          @PathVariable("chatIdx") Long chatIdx,
                          Authentication authentication) throws IOException {
        Member member = memberService.infoFromLogin(authentication);
        
        if (chatContentsIdx == null) {
            chatService.addChatContent(chatIdx, chatContents, member, replyIdx, file);
        } else {
            chatService.addNotice(chatContentsIdx, member);
        }

        return "redirect:/chatRoom/" + chatIdx;
    }
    
    
    @PostMapping("/chatRoomCreate")
    public @ResponseBody Long chatRoomCreate(@RequestBody Long[] members,
    								Authentication authentication) {
    	Member member = memberService.infoFromLogin(authentication);
    	Long companyIdx = member.getCompany().getCompanyIdx();
    	chatService.addChatRoom(companyIdx);
    	Long chatIdx = chatService.mexChatIdx();
    	
    	
    	for(int i=0; i<members.length; i++) {
    		chatService.addChatMember(chatIdx, members[i]);
    	}
    	return chatIdx;
    }
	
	@GetMapping("/chatCollection")
	public String chatCollection(Model model, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		List<Chat> chatList = this.chatService.getList(member);
		model.addAttribute("chatList", chatList);
		return "chatCollection";
	}

	@GetMapping("/chatNoticeText.html")
	public String chatNotice(Model model) {
		return "chatNoticeText";
	}
}
