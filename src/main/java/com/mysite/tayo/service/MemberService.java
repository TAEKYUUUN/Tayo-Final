package com.mysite.tayo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.tayo.DataNotFoundException;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Organization;
import com.mysite.tayo.repository.CompanyRepository;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final OrganizationRepository organizationRepository;
	private final MemberRepository memberRepository;
	private final CompanyRepository companyRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Optional<Member> findMemberByEmail(String email){
		 return memberRepository.findByEmail(email);
	}
	
    public List<Member> getList() {
        return this.memberRepository.findAll();
    }

    public List<Member> getListByCompanyIdx(Long companyIdx) {
        return this.memberRepository.findByCompanyCompanyIdx(companyIdx);
    }
    
    // 특정 회사에 그 이름에 맞는 org인지 확인해서 추가하는 기능 필요할 것. 이름에서 역으로 찾아가기 때문에, 이름은 unique 해야할것.
    // 일단 company도 임시로 고정으로 들어가도록 설정해둠. 수정 요망
    public void create(String name, String email, String pw, String org, String rank,  String phone) {
    	Member member = new Member();
    	Optional<Organization> organization = organizationRepository.findById((long) 1);
    	Optional<Company> company = companyRepository.findById((long) 1);
    	if(organization.isPresent() && company.isPresent()) {
    	Date date = new Date();
    	member.setName(name);
    	member.setEmail(email);
    	member.setPassword(passwordEncoder.encode(pw));
    	member.setOrganization(organization.get());
    	member.setRankName(rank);
    	member.setPhone(phone);
    	member.setRegistDate(date);
    	member.setCompany(company.get());
    	this.memberRepository.save(member);
    	}else {
    		 throw new DataNotFoundException("Error.");
    	}
    }
    
    public void registMember(String name, String email, String pw, int randomNumber) {
    	Member member = new Member();
    	member.setName(name);
    	member.setEmail(email);
    	member.setPassword(passwordEncoder.encode(pw));
    	member.setCertificationNumber(randomNumber);
    	member.setIsConfirmed(1);
    	this.memberRepository.save(member);
    }
    
    
    public void update(String name, String email, String pw, String org, String rank,  String phone) {
    	Optional<Member> member = this.memberRepository.findByEmail(email);
    	Optional<Organization> organization = organizationRepository.findByOrganizationName(org);
    	if(member.isPresent()) {
    		member.get().setName(name);
        	member.get().setPassword(passwordEncoder.encode(pw));
    		if(organization.isPresent()) {
    			member.get().setOrganization(organization.get());
    		}
    		member.get().setRankName(rank);
    		member.get().setPhone(phone);
    		this.memberRepository.save(member.get());
    	}else {
    		throw new DataNotFoundException("에러");
    	}
    	
    }
    
    public Optional<Member> existTest(String email){
    		return this.memberRepository.findByEmail(email);
    }
    
    public void loginCertification(String email) {
    	Optional<Member> _member = memberRepository.findByEmail(email);
    	if(_member.isPresent()) {
    		Member member = _member.get();
    		Date date = new Date();
    		member.setIsConfirmed(null);
    		member.setCertificationNumber(null);
    		member.setRegistDate(date);
    		this.memberRepository.save(member);
    	} else {
    		throw new DataNotFoundException("에러");
    	}
    }
    
    public Member infoFromLogin(Authentication authentication) {
    	User user = (User) authentication.getPrincipal();  
        String email = user.getUsername();
        Optional<Member> optionalMember = findMemberByEmail(email);
        return optionalMember.get();
    }
}
