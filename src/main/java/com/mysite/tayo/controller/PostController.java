package com.mysite.tayo.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.repository.LowerTaskRepository;
import com.mysite.tayo.repository.ParagraphRepository;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.repository.ScheduleAttenderRepository;
import com.mysite.tayo.repository.ScheduleRepository;
import com.mysite.tayo.repository.TaskRepository;
import com.mysite.tayo.repository.TodoMemberRepository;
import com.mysite.tayo.repository.TodoNameRepository;
import com.mysite.tayo.repository.TodoRepository;
import com.mysite.tayo.repository.VoteItemRepository;
import com.mysite.tayo.repository.VoteRepository;
import com.mysite.tayo.repository.VoterRepository;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {

	private final PostService postService;
	private final MemberService memberService;
	private final ProjectRepository projectRepository;
	private final PostRepository postRepository;
	private final ParagraphRepository paragraphRepository;
	private final TaskRepository taskRepository;
	private final LowerTaskRepository lowerTaskRepository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleAttenderRepository scheduleAttenderRepository;
	private final TodoRepository todoRepository;
	private final TodoNameRepository todoNameRepository;
	private final TodoMemberRepository todoMemberRepository;
	private final VoteRepository voteRepository;
	private final VoteItemRepository voteItemRepository;
	private final VoterRepository voterRepository;
	
	
	@PostMapping("/projectFeed2/{projectIdx}")
	public String createPost(Authentication authentication, @PathVariable("projectIdx")Long projectIdx, 
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam("tabType") int tabType,
			@RequestParam("openRange") int openRange) {
		Optional<Project> project = projectRepository.findById(projectIdx);
		Member member = memberService.infoFromLogin(authentication);
		
		switch(tabType) {
		case 1:
			postService.createParagraph(member, project.get(), title, content, openRange);
		}
		return "redirect:/projectFeed2/{projectIdx}";
	}
}
