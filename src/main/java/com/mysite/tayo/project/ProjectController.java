package com.mysite.tayo.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
