package com.mysite.tayo;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.UserSessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Autowired
	private UserSessionService userSessionService;
	@Autowired
	private MemberRepository memberRepository;
	
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
        	 Object principal = authentication.getPrincipal();
             String email;

             if (principal instanceof UserDetails) {
                 email = ((UserDetails) principal).getUsername();
             } else {
                 email = principal.toString();
             }
        	
            Optional<Member> optionalMember = memberRepository.findByEmail(email);
            Member member = optionalMember.get();
        	userSessionService.saveLogoutInfo(member.getMemberIdx());
        	request.getSession().removeAttribute("USER_SESSION");
        }
        response.sendRedirect("/mainpage"); // 로그아웃 성공 후 리다이렉트할 URL
    }
}