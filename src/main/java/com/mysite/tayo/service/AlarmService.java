package com.mysite.tayo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.AlarmRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlarmService {
	private final AlarmRepository alarmRepository;
	
	public List<Alarm> findAlarmByMemberIdx(Member member) {

		Sort sort = Sort.by(Sort.Direction.DESC, "alarmTime");
		return alarmRepository.findByMemberMemberIdx(member, sort);
	}
}
