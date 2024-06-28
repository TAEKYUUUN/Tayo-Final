package com.mysite.tayo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProjectController {
	
	private final MemberService memberService;
	private final ProjectService projectService;
	private final ProjectRepository projectRepository;
	
	@GetMapping("/createNewProject")
	public String createNewProject() {
		return "createNewProject";
	}
	
	
	@PostMapping("/createNewProject")
	public String createProject(
			@RequestParam("projectName") String projectName,
			@RequestParam("main_tab") Integer mainTab,
			@RequestParam(value = "companyProject", defaultValue = "0") Integer projectType,
			@RequestParam(value = "needConfirm", defaultValue = "0") Integer withoutConfirm
//			Authentication authentication
			) {
//			User user = (User) authentication.getPrincipal();  
//        	String email = user.getUsername();
//        	Optional<Member> optionalMember = memberService.findMemberByEmail(email);
//        	Long memberIdx = optionalMember.get().getMemberIdx();
			Long memberIdx = 1l;
			projectService.createProject(projectName, mainTab, projectType, withoutConfirm, memberIdx);
		
        return "dashboard";
	}
	
	@GetMapping("/projectlist")
	public String getMyprojectList(Model model) {
		Long memberIdx = 1l;
		List<ProjectMember> projectList = this.projectService.getMyProject(memberIdx);
		List<Long> countMember = this.projectService.getCountProjectMember(projectList);
		model.addAttribute("projectList", projectList);
		model.addAttribute("countMember", countMember);
		return "MyProject";
	}
}
