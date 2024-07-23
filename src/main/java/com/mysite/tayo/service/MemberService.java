package com.mysite.tayo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.tayo.DataNotFoundException;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Organization;
import com.mysite.tayo.repository.CompanyRepository;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.OrganizationRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
    
    public String findNameByMemberIdx(Long memberIdx) {
    	Optional<Member> member = memberRepository.findById(memberIdx);
    	return member.get().getName();
    }
    // 특정 회사에 그 이름에 맞는 org인지 확인해서 추가하는 기능 필요할 것. 이름에서 역으로 찾아가기 때문에, 이름은 unique 해야할것.
    // 일단 company도 임시로 고정으로 들어가도록 설정해둠. 수정 요망
    public void create(String name, String email, Long OrganizationIdx, String pw, String rank,  String phone, Authentication authentication) {
    	Member member = new Member();
    	Member AuthenticationMember = infoFromLogin(authentication);
    	Long companyIdx = AuthenticationMember.getCompany().getCompanyIdx();
    	
    	Optional<Company> company = companyRepository.findById(companyIdx);
    	if(OrganizationIdx != null) {
    		Optional<Organization> organization = organizationRepository.findById(OrganizationIdx);
    		member.setOrganization(organization.get());
    	}
    	
    	Date date = new Date();
    	member.setName(name);
    	member.setEmail(email);
    	member.setPassword(passwordEncoder.encode(pw));
    	member.setRankName(rank);
    	member.setPhone(phone);
    	member.setRegistDate(date);
    	member.setCompany(company.get());
    	member.setIsAllowed(null);
    	this.memberRepository.save(member);
    	
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
     
    
    public void update(String name, String email, Long organizationIdx, String rank,  String phone) {
    	Optional<Member> member = this.memberRepository.findByEmail(email);
    	Optional<Organization> organization = organizationRepository.findById(organizationIdx);
    	if(member.isPresent()) {
    		member.get().setName(name);
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
    	 Object principal = authentication.getPrincipal();
         String email;

         if (principal instanceof UserDetails) {
             email = ((UserDetails) principal).getUsername();
         } else {
             email = principal.toString();
         }
    	
        Optional<Member> optionalMember = findMemberByEmail(email);
        return optionalMember.get();
    }
    public void resetPw(String email) {
    	Optional<Member> _member = memberRepository.findByEmail(email);
    	if(_member.isPresent()) {
    		Member member = _member.get();
    		member.setPassword(passwordEncoder.encode("12345678"));
    		memberRepository.save(member);
    	}else {
    		throw new NoSuchElementException("No member found with userId: " + email);
    	}
    	
    }
    
    @Autowired
    private JavaMailSender mailSender;
//
//    public void sendMessage(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        message.setFrom("minseong2782@gmail.com");
//        mailSender.send(message);
//    }
    public void sendEmailWithImage(String to, String subject, String htmlContent, int code) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent.replace("${code}", String.valueOf(code)), true); // true indicates HTML
        helper.setFrom("minseong2782@gmail.com");
        mailSender.send(mimeMessage);
    }
    public void registerUser(String email, int certificationNumber) throws MessagingException, IOException {
        // 회원가입 로직 처리 (예: 사용자 저장)

        // 이메일 발송
        String subject = "Tayo 가입 인증 번호 메일입니다.";
        String htmlContent = new String(Files.readAllBytes(Paths.get("C:\\git\\Tayo-Final\\src\\main\\resources\\templates\\EmailTemplate.html"))); // 이메일 템플릿 경로

        sendEmailWithImage(email, subject, htmlContent, certificationNumber);
        System.out.println("메일 잘 갔어요");
    }
    
}
