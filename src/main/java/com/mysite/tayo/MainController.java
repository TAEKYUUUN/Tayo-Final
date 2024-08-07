package com.mysite.tayo;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.service.AlarmService;
import com.mysite.tayo.service.ChatService;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	private final MemberService memberService;
	private final ProjectService projectService;
	private final ChatService chatService; 
	private final AlarmService alarmService;
	
	@GetMapping("/notAllowed")
	public String notAllowed() {
		return "notAllowed";
	}
	
	@GetMapping("/tayo")
	@ResponseBody
	public String hello() {
		return "for(int i=YG; i<=취업; i++)는 할 수 있다!";
	}
	
	@GetMapping("/")
	public String defaultAccess(Authentication authentication, Model model) {
		if (authentication != null && authentication.isAuthenticated() 
			    && !(authentication.getPrincipal() instanceof String)) {
			Member member = memberService.infoFromLogin(authentication);
			if(member.getCompany() != null) {
				model.addAttribute("member", member);
				List<ProjectMember> myProject = projectService.getMyProject(member.getMemberIdx());
				List<Member> companyMember = memberService.getListByCompanyIdx(member.getCompany().getCompanyIdx());
				List<Alarm> alarmList = alarmService.findAlarmByMemberIdx(member);
				model.addAttribute("myproject", myProject);
				model.addAttribute("companyMember", companyMember);
				model.addAttribute("AlarmList", alarmList);
				return "dashboard";
			}else {
				return "redirect:/member/logout";
			}
		} else {
			return "mainpage";
		}
	}
	
	@GetMapping("/mainpage")
	public String mainpage() {
		return "mainpage";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		Member member = memberService.infoFromLogin(authentication);
		System.out.println(member.getEmail());
		model.addAttribute("member", member);
		List<ProjectMember> myProject = projectService.getMyProject(member.getMemberIdx());
		List<Member> companyMember = memberService.getListByCompanyIdx(member.getCompany().getCompanyIdx());
		List<Chat> chatList = chatService.getList(member);
		
	    // 각 채팅방의 마지막 메시지와 시간을 추가
	    for (Chat chat : chatList) {
	        ChatContents lastContent = chat.getLastChatContents();
	        if (lastContent != null) {
	            chat.setLastChatContents(lastContent);
	            chat.setLastChatTime(chat.getLastChatTime());
	        } else {
	            chat.setLastChatContents(null);
	            chat.setLastChatTime("없음");
	        }
	        chat.setUnreadCount(chat.getUnreadCount(member));
	    }
		
		List<Alarm> alarmList = alarmService.findAlarmByMemberIdx(member);
		model.addAttribute("chatList", chatList);
		model.addAttribute("myproject", myProject);
		model.addAttribute("companyMember", companyMember);
		model.addAttribute("AlarmList", alarmList);
		
		return "dashboard";
	}
	
	@GetMapping("/createOrJoinCompany")
	public String createOrJoinCompany() {
		return "createOrJoinCompany";
	}
//	주석주석
	
}
