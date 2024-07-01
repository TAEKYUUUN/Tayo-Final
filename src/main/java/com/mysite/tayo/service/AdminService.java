package com.mysite.tayo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Organization;
import com.mysite.tayo.repository.CompanyRepository;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private MemberService memberService;
	
	public Company updateCompanyInfo(Company company) {
		Optional<Company> _updatedCompany = companyRepository.findById(company.getCompanyIdx());
		if (_updatedCompany.isPresent()) {
            Company updatedCompany = _updatedCompany.get();
            updatedCompany.setCompanyName(company.getCompanyName());
            updatedCompany.setParticipationMethod(company.getParticipationMethod());
            updatedCompany.setUrl(company.getUrl());
            companyRepository.save(updatedCompany);
            return updatedCompany;
        } else {
            throw new RuntimeException("Company not found with id: " + company.getCompanyIdx());
        }
	}
	
	public void addAllPeople(List<Member> members, Authentication authentication) {
		Member loginMember = memberService.infoFromLogin(authentication);
		Company memberCompany = loginMember.getCompany();
		for(int i = 0; i< members.size(); i++) {
			Member memberNow = members.get(i);
			Optional<Organization> nowOrganization = organizationRepository.findByOrganizationName(memberNow.getOrganization().getOrganizationName());
			Member newMember = new Member();
			newMember.setName(memberNow.getName());
			newMember.setEmail(memberNow.getEmail());
			newMember.setCompany(memberCompany);
			if(memberNow.getPhone() != null) {
				newMember.setPhone(memberNow.getPhone());
			}
			if(nowOrganization.isPresent()) {
				newMember.setOrganization(nowOrganization.get());
			}
			if(memberNow.getRankName() != null) {
				newMember.setRankName(memberNow.getRankName());
			}
			if(memberNow.getPhoneCompany() != null) {
				newMember.setPhoneCompany(memberNow.getPhoneCompany());
			}
			memberRepository.save(newMember);
		}
	}
}
