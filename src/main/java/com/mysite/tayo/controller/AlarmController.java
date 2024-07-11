package com.mysite.tayo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.repository.AlarmRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AlarmController {
	private final AlarmRepository alarmRepository;
	
	@PutMapping("/AlarmRead")
	@ResponseStatus(HttpStatus.OK) 
	public void alarmIsRead(@RequestBody Long alarmIdx) {
		Alarm alarm = alarmRepository.findById(alarmIdx).get();
		alarm.setIsRead(null);
		alarmRepository.save(alarm);
	}
}
