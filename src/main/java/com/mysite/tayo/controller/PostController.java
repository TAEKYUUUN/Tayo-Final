package com.mysite.tayo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.repository.LowerTaskRepository;
import com.mysite.tayo.repository.MemberRepository;
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
	private final MemberRepository memberRepository;
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
	public String createPost(Authentication authentication, @PathVariable("projectIdx") Long projectIdx,
	        @RequestParam("tabType") int tabType,
	        @RequestParam("title") String title,
	        @RequestParam("openRange") int openRange,
	        @RequestParam(value = "content", required = false) String content,
	        @RequestParam(value = "condition", required = false) Integer condition,
	        @RequestParam(value = "managerIdx", required = false) Long managerIdx,
	        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
	        @RequestParam(value = "place", required = false) String place,
	        @RequestParam(value = "voteDetail", required = false) String voteDetail,
	        @RequestParam(value = "lowerTaskNames", required = false) List<String> lowerTaskNameList,
	        @RequestParam(value = "lowerTaskConditions", required = false) List<Integer> lowerTaskConditionList,
	        @RequestParam(value = "scheduleAttenders", required = false) List<Member> scheduleAttenderList,
	        @RequestParam(value = "todoNames", required = false) List<String> todoNameList,
	        @RequestParam(value = "todoManagers", required = false) List<Member> todoManagerList,
	        @RequestParam(value = "todoDeadlines", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") List<Date> todoDeadlineList,
	        @RequestParam(value = "voteEnddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date voteEnddate,
	        @RequestParam(value = "voteItems", required = false) List<String> voteItemList) {

	    Member member = memberService.infoFromLogin(authentication);
	    Optional<Project> projectOpt = projectRepository.findById(projectIdx);
	    
	    if (!projectOpt.isPresent()) {
	        // Handle project not found
	        return "redirect:/error";
	    }

	    Project project = projectOpt.get();
	    
	    switch (tabType) {
	        case 1: // paragraph
	            postService.createParagraph(member, project, title, content, openRange);
	            break;
	        case 2: // task
	            if (managerIdx == null || !memberRepository.findById(managerIdx).isPresent()) {
	                // Handle manager not found
	                return "redirect:/error";
	            }
	            Member manager = memberRepository.findById(managerIdx).get();
	            postService.createTask(member, project, title, condition, manager, endDate, content, lowerTaskNameList, lowerTaskConditionList);
	            break;
	        case 3: // schedule
	            postService.createSchedule(member, project, title, startDate, endDate, place, content, scheduleAttenderList);
	            break;
	        case 4: // todo
	            postService.createTodo(member, project, title, todoNameList, todoManagerList, todoDeadlineList);
	            break;
	        case 5: // vote
	            postService.createVote(member, project, title, voteDetail, voteEnddate, voteItemList);
	            break;
	        default:
	            // Handle invalid tabType
	            return "redirect:/error";
	    }
	    return "redirect:/projectFeed2/" + projectIdx;
	}

}

