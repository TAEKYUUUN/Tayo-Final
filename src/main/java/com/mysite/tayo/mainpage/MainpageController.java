package com.mysite.tayo.mainpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainpageController {

	@GetMapping("/main")
	public String main() {
		return "mainpage";	
		}
	
}
