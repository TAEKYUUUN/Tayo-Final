package com.mysite.tayo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.tayo.DTO.OrganizationDTO;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Organization;
import com.mysite.tayo.repository.OrganizationRepository;
import com.mysite.tayo.service.AdminService;
import com.mysite.tayo.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Admin")
@RequiredArgsConstructor
@Controller
public class AdminController {
	private final MemberService memberService;
	private final AdminService adminService;
	private final OrganizationRepository organizationRepository;
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

	
	@GetMapping("/list")
	public String list(Model model,  Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Long companyIdx = member.getCompany().getCompanyIdx();
		List<Member> memberList = this.memberService.getListByCompanyIdx(companyIdx);
		model.addAttribute("memberList", memberList);
		return "memberList";
	}
	
	@GetMapping("/memberAddAll")
	public String memberAddAll() {
		return "/Admin/memberAddAll";
	}
	
	@PostMapping("/memberAddAll")
	public String  postMemberAddAll(@RequestBody List<Member> members, Authentication authentication) {
		System.out.println(members.get(0).getName());
		adminService.addAllPeople(members, authentication);
		return "redirect:/Admin/list";
	}
	
	
	@PostMapping("/list")
	public String addMember(
            @RequestParam("popupName") String name,
            @RequestParam("popupEmail") String email,
            @RequestParam("popupPw") String pw,
            @RequestParam("popupOrg") String org,
            @RequestParam("popupRank") String rank,
            @RequestParam("popupPhone") String phone, Authentication authentication) {
		Optional<Member> member = memberService.existTest(email);
		
		if(member.isPresent()) {
			memberService.update(name, email, pw, org, rank, phone);
		}
		else {
			memberService.create(name, email, pw, org, rank, phone, authentication);
		}
		return "redirect:/Admin/list";
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

        List<Organization> organizationList = organizationRepository.findByCompanyCompanyIdx(company.getCompanyIdx());
		model.addAttribute("organizationList", organizationList);
        return "Admin/AdminOrganization";
	}

	@PostMapping("/AdminOrganization")
	public @ResponseBody List<OrganizationDTO> organizationAdd(@RequestBody OrganizationDTO organizationDTO, Authentication authentication) {
		Member member = memberService.infoFromLogin(authentication);
		Company company = member.getCompany();
		
		if(organizationDTO.getAction().equals("create")) {
			Optional<Organization> upperOrganization = organizationRepository.findById(organizationDTO.getUpperOrganization());
			
			Organization organization = new Organization();
			organization.setOrganizationName(organizationDTO.getOrganizationName());
			organization.setCompany(company);
			if(upperOrganization.isPresent()) {
				organization.setUpperOrganization(upperOrganization.get());
			}
			organizationRepository.save(organization);
		} else if(organizationDTO.getAction().equals("update")) {
			Optional<Organization> _organization = organizationRepository.findById(organizationDTO.getOrganizationIdx());
			Organization organization = _organization.get();
			organization.setOrganizationName(organizationDTO.getOrganizationName());
			organizationRepository.save(organization);
		}
		
		
		
		List<Organization> organizationList = organizationRepository.findByCompanyCompanyIdx(company.getCompanyIdx());
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
