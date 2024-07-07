package com.mysite.tayo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.UserSession;
import com.mysite.tayo.repository.UserSessionRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserSessionService {
	 @Autowired
	 private UserSessionRepository userSessionRepository;
	 
	 public Optional<UserSession> findUserSessionByMemberIdx(Long memberIdx){
		 return userSessionRepository.findTopByMemberMemberIdxOrderBySessionIdxDesc(memberIdx);
	 }
	 
	 public Optional<UserSession> findUserSessionByMemberIdxAndDeviceType(Long memberIdx, String deviceType){
		 return userSessionRepository.findTopByMemberMemberIdxAndDeviceTypeOrderBySessionIdxDesc(memberIdx, deviceType);
	 }
	 
	 public void saveLoginInfo(Member member, String deviceType, String deviceName, String accessIp) {
	     UserSession userSession = new UserSession();
	     userSession.setDeviceType(deviceType);
	     userSession.setDeviceName(deviceName);
	     userSession.setLoginTime(LocalDateTime.now());
	     userSession.setMember(member);
	     userSession.setAccessIp(accessIp);
	     userSessionRepository.save(userSession);
	 }
	 
	 public void saveLogoutInfo(Long memberIdx) {
		 Optional<UserSession> _userSession = userSessionRepository. findTopByMemberMemberIdxOrderBySessionIdxDesc(memberIdx);
	     if (_userSession.isPresent()) {
	    	 UserSession userSession = _userSession.get();
	    	 System.out.println(userSession.getDeviceName());
	         userSession.setLogoutTime(LocalDateTime.now());
	         userSessionRepository.save(userSession);
	     }
	 }
	 
	 public String getAccessIP(HttpServletRequest request) {
			String ip = request.getHeader("X-Forwarded-For");

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
	 }
}
