package com.mysite.tayo.service;

import org.springframework.stereotype.Service;

import com.mysite.tayo.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	
	
	
}
