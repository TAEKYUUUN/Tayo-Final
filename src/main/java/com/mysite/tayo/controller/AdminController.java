package com.mysite.tayo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.tayo.DTO.OrganizationDTO;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Organization;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.entity.UserSession;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.OrganizationRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.AdminService;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.OrganizationService;
import com.mysite.tayo.service.UserSessionService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Admin")
@RequiredArgsConstructor
@Controller
public class AdminController {
	private final MemberService memberService;
	private final AdminService adminService;
	private final OrganizationService organizationService;
	private final UserSessionService userSessionService;
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final MemberRepository memberRepository;
	private final OrganizationRepository organizationRepository;
	
	@GetMapping("/adminProjectControl")
	public String projectInfo(Model model, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		List<Project> projectList =  projectRepository.findByCompanyCompanyIdx(company.getCompanyIdx());
		ArrayList<Integer> commentsCount = new ArrayList<Integer>();
		for (int i = 0; i < projectList.size(); i++) {
			Integer count = 0;
			List<Post> postList = projectList.get(i).getPostList();
			for (int j = 0; j < postList.size(); j++) {
				count += postList.get(j).getComments().size();
			}
			commentsCount.add(count);
		}
		List<Integer> comments = commentsCount; 
		model.addAttribute("comments", comments);
		model.addAttribute("projectList", projectList);
		return "/Admin/adminProjectControl";
	}
	
	@PostMapping("/adminProjectControl")
	public @ResponseBody Project projectDetail(@RequestBody Project project) {
		Optional<Project> _project = projectRepository.findById(project.getProjectIdx());
		Project newProject = _project.get();
		Hibernate.initialize(newProject.getProjectMemberList());
		 newProject.getProjectMemberList().forEach(member -> {
		        Hibernate.initialize(member.getMember());
		        Hibernate.initialize(member.getMember().getOrganization());
		    });
	    return newProject;
	}
	
	@PutMapping("/adminProjectControl")
	public @ResponseBody List<ProjectMember> projectUpdate(@RequestBody List<ProjectMember> projectMember) {
		Long projectIdx = projectMember.get(0).getProject().getProjectIdx();
		Optional<Project> project = projectRepository.findById(projectIdx);
		Project targetProject = project.get();
		targetProject.setProjectName(projectMember.get(0).getProject().getProjectName());
		for(int i = 0; i<targetProject.getProjectMemberList().size(); i++) {
			targetProject.getProjectMemberList().get(i).setIsManager(null);
		}
		projectRepository.save(targetProject);
		for(int i = 0; i<projectMember.size(); i++) {
			Optional<ProjectMember> _updateProjectMember = projectMemberRepository.findByProjectProjectIdxAndMemberMemberIdx(projectIdx, projectMember.get(i).getMember().getMemberIdx());
			ProjectMember updateProjectMember = _updateProjectMember.get();
			updateProjectMember.setIsManager(1);
			projectMemberRepository.save(updateProjectMember);
		}
		return projectMember;
	}
	
	
	
	@GetMapping("/AdminCompanyInfo")
	public String companyInfo(Model model, Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);
	    Company company = member.getCompany();
	    model.addAttribute("Company", company);
	    return "/Admin/AdminCompanyInfo";  
	}
	
	@PostMapping("/AdminCompanyInfo")
	public Company updateCompanyInfo(@RequestBody Company company) {
	    return adminService.updateCompanyInfo(company);
	}

	
	@GetMapping("/memberList")
	public String list(Model model,  Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Long companyIdx = member.getCompany().getCompanyIdx();
		List<Member> memberList = this.memberService.getListByCompanyIdx(companyIdx);
		Company company = member.getCompany();
		List<Member> companyMember = company.getMemberList();
		List<UserSession> userSessionList = new ArrayList<UserSession>();
		List<UserSession> userMobileSessionList = new ArrayList<UserSession>();
		List<UserSession> userComputerSessionList = new ArrayList<UserSession>();
		Integer[] memberCount = {0, 0, 0};
		
		
		
		for(int i = 0; i<companyMember.size(); i++) {
			Optional<UserSession> _userSession = userSessionService.findUserSessionByMemberIdx(companyMember.get(i).getMemberIdx());
			Optional<UserSession> _userMobileSession = userSessionService.findUserSessionByMemberIdxAndDeviceType(companyMember.get(i).getMemberIdx(), "Mobile");
			Optional<UserSession> _userComputerSession = userSessionService.findUserSessionByMemberIdxAndDeviceType(companyMember.get(i).getMemberIdx(), "Computer");
			if(_userSession.isPresent()) {
				userSessionList.add(_userSession.get());
				if(_userMobileSession.isPresent()) {
					userMobileSessionList.add(_userMobileSession.get());
				}
				if(_userComputerSession.isPresent()) {
					userComputerSessionList.add(_userComputerSession.get());
				}
			}
			if(companyMember.get(i).getIsBanned() == null && companyMember.get(i).getIsAllowed() == null) {
				memberCount[0] ++;
			}else if(companyMember.get(i).getIsBanned() == null && companyMember.get(i).getIsAllowed() != null) {
				memberCount[1] ++;
			}else {
				memberCount[2] ++;
			}
		}
		List<Organization> organizationList = organizationService.findByCompanyIdx(companyIdx);
		model.addAttribute("memberCount", memberCount);
		model.addAttribute("organizationList", organizationList);
		model.addAttribute("userComputerSessionList", userComputerSessionList);
		model.addAttribute("userMobileSessionList", userMobileSessionList);
		model.addAttribute("userSessionList", userSessionList);
		model.addAttribute("memberList", memberList);
		return "/Admin/memberList";
	}
	
	@PostMapping("/AllowMember")
	public @ResponseBody Member allowMember(@RequestBody Long memberIdx) {
		Member member = memberRepository.findById(memberIdx).get();
		member.setIsAllowed(null);
		memberRepository.save(member);
		return member;
	}
	
	@PostMapping("/DenyMember")
	public @ResponseBody Member denyMember(@RequestBody Long memberIdx) {
		Member member = memberRepository.findById(memberIdx).get();
		member.setIsAllowed(null);
		member.setCompany(null);
		memberRepository.save(member);
		return member;
	}
	@PostMapping("/noMoreCompanyMember")
	public @ResponseBody Member noMoreCompanyMember(@RequestBody Long memberIdx) {
		Member member = memberRepository.findById(memberIdx).get();
		member.setIsAllowed(null);
		member.setCompany(null);
		member.setRankName(null);
		member.setOrganization(null);
		member.setPhoneCompany(null);
		member.setIsBanned(null);
		member.setIsCompanyManager(null);
		memberRepository.save(member);
		return member;
	}
	@PostMapping("/organizationChange")
	public @ResponseBody Member noMoreCompanyMember(@RequestBody List<Long> newOrg) {
		Long memberIdx = newOrg.get(0);
		Long organizationIdx = newOrg.get(1);
		Member member = memberRepository.findById(memberIdx).get();
		Organization organization = organizationRepository.findById(organizationIdx).get();
		member.setOrganization(organization);
		memberRepository.save(member);
		return member;
	}
	
	@PostMapping("/AdminMemberBan")
	public @ResponseBody Member banMember(@RequestBody Long memberIdx) {
		Member member = memberRepository.findById(memberIdx).get();
		if(member.getIsBanned() == null) {
			member.setIsBanned(1);
		}else {
			member.setIsBanned(null);
		}
		memberRepository.save(member);
		return member;
	}
	
	@PostMapping("/AdminMemberManager")
	public @ResponseBody Member managerOrNot(@RequestBody Long memberIdx) {
		Member member = memberRepository.findById(memberIdx).get();
		if(member.getCompany().getManager().getMemberIdx() == member.getMemberIdx()) {
			return member;
		}else if(member.getIsCompanyManager() ==null){
			member.setIsCompanyManager(1);
		}else {
			member.setIsCompanyManager(null);
		}
		memberRepository.save(member);
		return member;
	}
	
	@GetMapping("/memberAddAll")
	public String memberAddAll() {
		return "/Admin/memberAddAll";
	}
	
	@PostMapping("/memberAddAll")
	public String  postMemberAddAll(@RequestBody List<Member> members, Authentication authentication) {
		System.out.println(members.get(0).getName());
		adminService.addAllPeople(members, authentication);
		return "redirect:/Admin/memberList";
	}
	@PutMapping("/memberList")
	public @ResponseBody String passwordReset(@RequestBody String theEmail){
		theEmail = theEmail.replace("\"", ""); // 큰따옴표 제거
        memberService.resetPw(theEmail);
        return theEmail;
	}
	
	@PostMapping("/memberList")
	public String addMember(
            @RequestParam("popupName") String name,
            @RequestParam("popupEmail") String email,
            @RequestParam(value="popupRank", required = false) String rank,
            @RequestParam(value="popupPw", required = false) String pw,
            @RequestParam(value="popupPhone", required = false) String phone, 
            @RequestParam(value="popupOrgIdx", required = false) Long organizationIdx,Authentication authentication) {
		Optional<Member> member = memberService.existTest(email);
		
		if(member.isPresent()) {
			memberService.update(name, email, organizationIdx, rank, phone);
		}
		else {
			memberService.create(name, email, organizationIdx, pw, rank, phone, authentication);
		}
		return "redirect:/Admin/memberList";
	}
	
	@GetMapping("/AdminInvitation")
	public String invitation(Model model, Authentication authentication){
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		model.addAttribute("Company",company);
		return "/Admin/AdminInvitation";
	}
	
	@GetMapping("/AdminOrganization")
	public String organization(Model model, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		Long companyIdx = company.getCompanyIdx();
        List<Organization> organizationList = organizationService.findByCompanyIdx(companyIdx);
		model.addAttribute("organizationList", organizationList);
        return "Admin/AdminOrganization";
	}

	@PostMapping("/AdminOrganization")
	public @ResponseBody List<OrganizationDTO> organizationAdd(@RequestBody OrganizationDTO organizationDTO, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		
		if(organizationDTO.getAction().equals("create")) {
			Optional<Organization> upperOrganization = organizationService.findByOrganizationIdx(organizationDTO.getUpperOrganization());
			organizationService.createOrganization(organizationDTO, company, upperOrganization);
			
		} else if(organizationDTO.getAction().equals("update")) {
			organizationService.updateOrganization(organizationDTO);
			
		}else if(organizationDTO.getAction().equals("delete")) {
			organizationService.deleteOrganization(organizationDTO);
			
		}else if(organizationDTO.getAction().equals("clear")) {
			organizationService.deleteOrganizationsByCompanyIdx(company.getCompanyIdx());
		}
		
		
		
		List<Organization> organizationList = organizationService.findByCompanyIdx(company.getCompanyIdx());
		List<OrganizationDTO> organizationDTOList = organizationList.stream().map(org -> {
			OrganizationDTO dto = new OrganizationDTO();
			dto.setOrganizationIdx(org.getOrganizationIdx());
			dto.setOrganizationName(org.getOrganizationName());
			dto.setUpperOrganization(org.getUpperOrganization() != null ? org.getUpperOrganization().getOrganizationIdx() : null);
			return dto;
		}).collect(Collectors.toList());
		return organizationDTOList;
	}
}
