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
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.UserSession;
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
	
	@GetMapping("/adminProjectControl")
	public String projectInfo(Model model, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		List<Project> projectList =  projectRepository.findByCompanyCompanyIdx(company.getCompanyIdx());
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
		}
		List<Organization> organizationList = organizationService.findByCompanyIdx(companyIdx);
		model.addAttribute("organizationList", organizationList);
		model.addAttribute("userComputerSessionList", userComputerSessionList);
		model.addAttribute("userMobileSessionList", userMobileSessionList);
		model.addAttribute("userSessionList", userSessionList);
		model.addAttribute("memberList", memberList);
		return "/Admin/memberList";
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
            @RequestParam("popupRank") String rank,
            @RequestParam(value="popupPw", required = false) String pw,
            @RequestParam("popupPhone") String phone, 
            @RequestParam("popupOrgIdx") Long organizationIdx,Authentication authentication) {
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
