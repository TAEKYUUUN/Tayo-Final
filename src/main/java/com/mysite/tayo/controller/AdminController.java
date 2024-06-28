package com.mysite.tayo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Admin")
@RequiredArgsConstructor
@Controller
public class AdminController {
	private final MemberService memberService;
	
	@GetMapping("/AdminCompanyInfo")
	public String companyInfo() {
		return "AdminCompanyInfo";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		List<Member> memberList = this.memberService.getList();
		model.addAttribute("memberList", memberList);
		return "memberList";
	}
	
	@GetMapping("/AddAll")
	public String memberAddAll() {
		return "memberAddAll";
	}
	
	@PostMapping("/list")
	public String addMember(
            @RequestParam("popupName") String name,
            @RequestParam("popupEmail") String email,
            @RequestParam("popupPw") String pw,
            @RequestParam("popupOrg") String org,
            @RequestParam("popupRank") String rank,
            @RequestParam("popupPhone") String phone) {
		Optional<Member> member = memberService.existTest(email);
		
		if(member.isPresent()) {
			memberService.update(name, email, pw, org, rank, phone);
		}
		else {
			memberService.create(name, email, pw, org, rank, phone);
		}
		return "redirect:/Admin/list";
	}
}
