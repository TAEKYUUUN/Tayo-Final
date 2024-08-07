package com.mysite.tayo;


import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    public CustomAuthenticationSuccessHandler(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         User user = (User) authentication.getPrincipal();  // 로그인된 사용자 정보 가져오기
         String email = user.getUsername();  // 여기서 email이 반환됩니다.
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (member.getIsConfirmed() != null && member.getIsConfirmed() == 1) {
                response.sendRedirect("/member/certification");
            } else if(member.getCompany() == null) {
            	response.sendRedirect("/createOrJoinCompany");
            } else if(member.getIsAllowed() != null) {
            	response.sendRedirect("/notAllowed");
            } else {
            	response.sendRedirect("/dashboard");
            }
        } else {
            response.sendRedirect("/member/login?error=true");
        }
    }
}

