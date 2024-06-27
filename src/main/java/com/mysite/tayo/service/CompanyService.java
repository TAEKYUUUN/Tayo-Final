package com.mysite.tayo.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.CompanyRepository;
import com.mysite.tayo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompanyService {
	
	private final CompanyRepository companyRepository;
	private final MemberRepository memberRepository;
	// 알파벳으로만 이루어져있는지 확인해주는 정규식 메서드
	public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
	
	//회사명, 사람idx 받아와서 회사명, 사람idx, 자동 생성 url 넣고 회사 객체 생성
	public void companyCreate(String companyName, Long memberIdx) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    Random random = new Random();
	    Optional<Member> member = memberRepository.findById(memberIdx);
		Company company = new Company();
		company.setCompanyName(companyName);
		company.setManager(member.get());
		if(isAlpha(companyName)) {
			while(true) {
				StringBuilder sb = new StringBuilder(5);
		        for (int i = 0; i < 5; i++) {
		            sb.append(characters.charAt(random.nextInt(characters.length())));
		        }
				String companyURL = companyName + sb.toString();
				Optional<Company> _company = companyRepository.findByUrl(companyURL);
				if(_company.isPresent()) {
					continue;
				} else {
					company.setUrl(companyURL);
					break;
				}
			}
		}else {
			while(true) {
				StringBuilder sb = new StringBuilder(10);
		        for (int i = 0; i < 10; i++) {
		            sb.append(characters.charAt(random.nextInt(characters.length())));
		        }
				String companyURL = sb.toString();
				Optional<Company> _company = companyRepository.findByUrl(companyURL);
				if(_company.isPresent()) {
					continue;
				} else {
					company.setUrl(companyURL);
					break;
				}
			}
		}
		companyRepository.save(company);
	}
	
	
	public Optional<Company> findCompanyByUrl(String url){
		return companyRepository.findByUrl(url);
	}
	public Optional<Member> findMemberByEmail(String email){
		return memberRepository.findByEmail(email);
	}
	public void saveMember(Member member) {
		memberRepository.save(member);
	}
	public Optional<Company> findCompanyByCompanyManagerIdx(Member member){
		return companyRepository.findByManagerMemberIdx(member.getMemberIdx());
	}
}
