package com.mysite.tayo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProjectController {
	
	private final ProjectService projectService;
	private final ProjectRepository projectRepository;
	
	@GetMapping("/createNewProject")
	public String createNewProject() {
		return "createNewProject";
	}
}
