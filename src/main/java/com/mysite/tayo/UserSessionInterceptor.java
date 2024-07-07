package com.mysite.tayo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.UserSessionService;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class UserSessionInterceptor implements HandlerInterceptor  {
	
	 @Autowired
	 private UserSessionService userSessionService;
	 @Autowired
	 private MemberService memberService;
	 
	 @Override
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		 
		 Authentication authentication = null;
		 if(SecurityContextHolder.getContext().getAuthentication() != null) {
			 authentication = SecurityContextHolder.getContext().getAuthentication();
		 }
		 if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
			 HttpSession session = request.getSession();
			 // 세션이 이미 존재하는지 확인
			 if (session.getAttribute("USER_SESSION") == null) {
				 Member member = memberService.infoFromLogin(authentication);
			     String userAgentString = request.getHeader("User-Agent");
			     UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
			     String deviceType = extractDeviceType(userAgent);
		         String deviceName = extractDeviceName(userAgent);
		         String accessIp = userSessionService.getAccessIP(request);
			     userSessionService.saveLoginInfo(member, deviceType, deviceName, accessIp);
			     session.setAttribute("USER_SESSION", member.getName());
			 }
		 }
	     return true;
	 }

	private String extractDeviceType(UserAgent userAgent) {
		DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
		return deviceType != null ? deviceType.getName() : "Unknown Device Type";
	}

	private String extractDeviceName(UserAgent userAgent) {
		String osName = userAgent.getOperatingSystem().getName();
		return (osName != null ? osName : "Unknown OS");
	}
}
