package com.mysite.tayo.member;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("/certification")
	public String certification() {
		return "certification";
	}
	
	@PostMapping("/certification")
	public String certificationCheck(
			@RequestParam("digit1") String digit1, 
			@RequestParam("digit2") String digit2,
			@RequestParam("digit3") String digit3,
			@RequestParam("digit4") String digit4,
			@RequestParam("digit5") String digit5,
			@RequestParam("digit6") String digit6, HttpServletRequest request, Model model, Authentication authentication
			) {
		String email = "";
		String certificationNumber = "";
			if (authentication != null && authentication.isAuthenticated() 
			    && !(authentication.getPrincipal() instanceof String)) {
				 User user = (User) authentication.getPrincipal();  
		         email = user.getUsername();
		         Optional<Member> optionalMember = memberService.findMemberByEmail(email);
		         if(optionalMember.isPresent()) {
		        	 Member member = optionalMember.get();
		        	 certificationNumber = member.getCertificationNumber() + "";
		         }
		         
			} else {
				HttpSession session = request.getSession();
				certificationNumber = session.getAttribute("certificationNumber") + "";
				email = (String) session.getAttribute("email");
				System.out.println(certificationNumber);
				System.out.println(email);
			}
		
		
		String code = digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
		
		if(certificationNumber == null || !certificationNumber.equals(code) ) {
			 model.addAttribute("error", "인증번호가 일치하지 않습니다.");
		     return "certification";
		}
		else {
			memberService.loginCertification(email);
		}
		return "redirect:/member/login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "LoginPage";
	}
	@GetMapping("/list")
	public String list(Model model) {
		List<Member> memberList = this.memberService.getList();
		model.addAttribute("memberList", memberList);
		return "memberList";
	}
	
	@GetMapping("/regist")
	public String regist(MemberCreateForm memberCreateForm) {
		return "Regist";
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
		return "redirect:/member/list";
	}
	
	@PostMapping("/regist")
	public String registMember(
			@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult,  HttpServletRequest request
			) {
		if(bindingResult.hasErrors()) {
			return "Regist";
		}
		if(!memberCreateForm.getPassword().equals(memberCreateForm.getCheckpw())) {
			bindingResult.rejectValue("checkpw", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "Regist";
		}
		int randomNumber = (int)(Math.random()*900000)+100000;
		memberService.registMember(memberCreateForm.getName(), memberCreateForm.getEmail(), memberCreateForm.getPassword(), randomNumber);
		HttpSession session = request.getSession();
		session.setAttribute("certificationNumber", randomNumber);
		session.setAttribute("email", memberCreateForm.getEmail());
		
		return "redirect:/member/certification";
	}
	
}
