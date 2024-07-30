package com.mysite.tayo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysite.tayo.DTO.AddChatMemberRequest;
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
				.sorted(Comparator.comparing(ChatContents::getTime)).collect(Collectors.toList());

		Long maxNotice = chatService.maxNotice(chatIdx);
		ArrayList<Long> chatDeleteOnlyForMeArr = chatService.chatDeleteOnlyForMeChatContentIdx(member);
		List<Long> chatDeleteOnlyForMeList = chatDeleteOnlyForMeArr;
		System.out.println(chatDeleteOnlyForMeList);

		for (int i = 0; i < chatDeleteOnlyForMeList.size(); i++) {
			for (int j = 0; j < sortedChatContentsList.size(); j++) {
				if (chatDeleteOnlyForMeList.get(i) == sortedChatContentsList.get(j).getChatContentsIdx()) {
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

	@GetMapping("/chatInvite/{chatIdx}")
	public String chatInvite(Model model, Authentication authentication, @PathVariable("chatIdx") Long chatIdx) {
		Member member = memberService.infoFromLogin(authentication);
		model.addAttribute("member", member);
		List<Member> companyMember = memberService.getListByCompanyIdx(member.getCompany().getCompanyIdx());
		Optional<Chat> _chatList = this.chatService.getId(chatIdx);
		Chat chatList = _chatList.get();

		for (int i = 0; i < companyMember.size(); i++) {
			for (int j = 0; j < chatList.getChatMemberList().size(); j++) {
				if (companyMember.get(i).getMemberIdx() == chatList.getChatMemberList().get(j).getMember()
						.getMemberIdx()) {
					companyMember.remove(i);
				}
			}
		}

		model.addAttribute("chatList", chatList);
		model.addAttribute("companyMember", companyMember);

		return "chatInvite";
	}

	@PostMapping("/chatInvite/{chatIdx}")
	public String addChatInvite(@PathVariable("chatIdx") Long chatIdx, Authentication authentication) {
		return "redirect:/chatInvite/" + chatIdx;
	}

	@PostMapping("/chatRoom/{chatIdx}")
	public String chatAdd(@RequestParam(value = "chatContents", required = false) String chatContents,
			@RequestParam(value = "replyIdx", required = false) Long replyIdx,
			@RequestParam(value = "chatContentsIdx", required = false) Long chatContentsIdx,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "chatName", required = false) String chatName,
			@RequestParam(value = "alarmSetting", required = false) Integer alarmSetting,
			@PathVariable("chatIdx") Long chatIdx, Authentication authentication) throws IOException {
		Member member = memberService.infoFromLogin(authentication);

		if (chatContentsIdx == null && chatName == null && alarmSetting == null) {
			chatService.addChatContent(chatIdx, chatContents, member, replyIdx, file);
			chatContentsIdx = chatService.maxChatContentIdx(member.getMemberIdx(), chatIdx);

			ArrayList<Long> chatMemberList = chatService.chatMemberList(chatIdx, member.getMemberIdx());
			for (int i = 0; i < chatMemberList.size(); i++) {
				chatService.addChatUnreader(chatContentsIdx, chatMemberList.get(i));
			}
		} else if (chatContentsIdx != null && chatName == null && alarmSetting == null) {
			chatService.addNotice(chatContentsIdx, member);
		} else if (chatName != null) {
			chatService.updateChatName(chatName, member.getMemberIdx(), chatIdx);
		} else if (alarmSetting != null) {
			chatService.updateAlarm(alarmSetting, member.getMemberIdx(), chatIdx);
		}

		return "redirect:/chatRoom/" + chatIdx;
	}

	@PostMapping("/chatRoomCreate")
	public @ResponseBody String chatRoomCreate(@RequestBody Long[] members, Authentication authentication)
			throws Exception {
		Member member = memberService.infoFromLogin(authentication);
		Long companyIdx = member.getCompany().getCompanyIdx();
		chatService.addChatRoom(companyIdx);
		Long chatIdx = chatService.maxChatIdx();
		ObjectMapper objectMapper = new ObjectMapper();

		String memberName = "";

		for (int i = 0; i < members.length; i++) {
			chatService.addChatMember(chatIdx, members[i]);
			if(members[i] != member.getMemberIdx()) {
				memberName += "[" + memberService.findNameByMemberIdx(members[i]) + "]";
			}
		}

		ObjectNode responseNode = objectMapper.createObjectNode();
		responseNode.put("chatIdx", chatIdx);
		responseNode.put("memberName", memberName);

		String responseMessage = objectMapper.writeValueAsString(responseNode);

		return responseMessage;
	}

	@PostMapping("/addChatMember")
	public ResponseEntity<String> addChatMember(@RequestBody AddChatMemberRequest request, Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);

	    try {
	        Long chatIdx = request.getChatIdx();
	        List<Long> members = request.getMembers();

	        for (Long memberId : members) {
	        	if(memberId != member.getMemberIdx() ) {
	        		chatService.addChatMember(chatIdx, memberId);
	        	}
	        }

	        return ResponseEntity.ok("Success");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
	    }
	}



}
