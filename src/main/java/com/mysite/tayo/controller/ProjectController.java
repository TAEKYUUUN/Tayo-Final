package com.mysite.tayo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.repository.ProjectMemberRepository;
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
	private final ProjectMemberRepository projectMemberRepository;
	
	// 테스트용
	@GetMapping("/projectFeed2/{projectIdx}")
	public String feed(Model model, Authentication authentication, @PathVariable("projectIdx")Long projectIdx) {
		Member member = memberService.infoFromLogin(authentication);
		Optional<Project> project = projectRepository.findById(projectIdx);
		List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectProjectIdx(projectIdx);
		Optional<ProjectMember> projectMember = projectMemberRepository.findByProjectProjectIdxAndMemberMemberIdx(projectIdx, member.getMemberIdx());
		model.addAttribute("member", member);
		model.addAttribute("projectMember", projectMember.get());
		model.addAttribute("projectMemberList", projectMemberList);
		model.addAttribute("project", project.get());
		return "projectFeed2";
	}

	
	// 프로젝트 생성 페이지 이동
	@GetMapping("/createNewProject")
	public String createNewProject() {
		return "createNewProject";
	}
	
	@PostMapping("/createNewProject")
	public String createProject(
			@RequestParam("projectName") String projectName,
			@RequestParam("main_tab") Integer mainTab,
			@RequestParam(value = "companyProject", defaultValue = "0") Integer projectType,
			@RequestParam(value = "needConfirm", defaultValue = "0") Integer withoutConfirm,
			Authentication authentication
			) {
			Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
			Company company = memberService.infoFromLogin(authentication).getCompany();
			projectService.createProject(projectName, mainTab, projectType, withoutConfirm, memberIdx, company);
		
        return "redirect:/dashboard";
	}
	
	@GetMapping("/projectlist")
	public String getMyprojectList(Model model, Authentication authentication) {
		Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
		List<ProjectMember> projectList = this.projectService.getMyProject(memberIdx);
		List<Long> countMember = this.projectService.getCountProjectMember(projectList);
		model.addAttribute("projectList", projectList);
		model.addAttribute("countMember", countMember);
		return "myProject";
	}
	
	// 즐찾 설정&해제
	@PostMapping("/projectlist") 
	public String updateHotlist(@RequestParam(value="isHotlist",  defaultValue = "") String isHotlist, @RequestParam(name="projectIdx") Long projectIdx, Authentication authentication) {
		Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
		Optional<ProjectMember> optionalMember = this.projectMemberRepository.findByProjectProjectIdxAndMemberMemberIdx(projectIdx, memberIdx);
		if (optionalMember.isPresent()) {
	        ProjectMember member = optionalMember.get();
	        if ("1".equals(isHotlist)) {
	            member.setHotlist(null);
	        } else {
	            member.setHotlist(1);
	        }
	        projectMemberRepository.save(member);
		}
		projectMemberRepository.save(optionalMember.get());
		return "redirect:/projectlist";
	}
	
	@GetMapping("/companyOpenProject")
	public String companyOpenProejct() {
		return "companyOpenProject";
	}
	
	@GetMapping("/projectFeed")
	public String projectFeed() {
		return "projectFeed";
	}
	
}
