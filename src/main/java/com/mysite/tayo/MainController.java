package com.mysite.tayo;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/tayo")
	@ResponseBody
	public String hello() {
		return "for(int i=YG; i<=취업; i++)는 할 수 있다!";
	}
	@GetMapping("/")
	public String defaultAccess(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated() 
			    && !(authentication.getPrincipal() instanceof String)) {
			return "dashboard";
		} else {
			return "mainpage";
		}
	}
	
	@GetMapping("/mainpage")
	public String mainpage() {
		return "mainpage";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}
	
	@GetMapping("/createOrJoinCompany")
	public String createOrJoinCompany() {
		return "createOrJoinCompany";
	}
//	주석주석
	
}
