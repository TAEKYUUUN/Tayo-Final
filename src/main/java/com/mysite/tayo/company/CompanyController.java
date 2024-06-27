package com.mysite.tayo.company;


import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.member.Member;
import com.mysite.tayo.member.MemberRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class CompanyController {
	private final CompanyService companyService;
	@GetMapping("/JoinCompany")
	public String joinCompany(){
		return "JoinCompany";
	}
	
	@PostMapping("/JoinCompany")
	public String postJoinCompany(@RequestParam("companyURL") String companyURL, Authentication authentication){
		Optional<Company> _company = companyService.findCompanyByUrl(companyURL);
		User user = (User) authentication.getPrincipal();  
        String email = user.getUsername();
        Optional<Member> optionalMember = companyService.findMemberByEmail(email);
		Member member;
        if(optionalMember.isPresent()) {
        	member = optionalMember.get();
        	if(_company.isPresent()) {
        		Company company = _company.get();
        		member.setCompany(company);
        		companyService.saveMember(member);
        		return "redirect:/dashboard";
        	}else {
        		return "/JoinCompany?error=true";
        	}
        }
		return "/JoinCompany";
	}
	
	@GetMapping("/CreateCompany")
	public String createCompany() {
		return "createCompany";
	}
	
	@PostMapping("/CreateCompany")
	public String createNewCompany(@RequestParam("companyName") String companyName, Authentication authentication) {
		User user = (User) authentication.getPrincipal();  
        String email = user.getUsername(); 
        Optional<Member> _member = companyService.findMemberByEmail(email);
        Member member = _member.get();
		companyService.companyCreate(companyName, member.getMemberIdx());
		Optional<Company> company = companyService.findCompanyByCompanyManagerIdx(member);
		member.setCompany(company.get());
		companyService.saveMember(member);
		return"dashboard";
	}
}
