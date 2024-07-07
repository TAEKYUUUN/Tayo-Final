package com.mysite.tayo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {

	@Autowired
    private MemberService memberService;
	@Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;


	
	@PostMapping("/projectFeed2/{projectIdx}")
	public String createPost(Authentication authentication, @PathVariable("projectIdx") Long projectIdx,
	        @RequestParam("tabType") int tabType,
	        @RequestParam("title") String title,
	        @RequestParam("openRange") int openRange,
	        @RequestParam(value = "content", required = false) String content,
	        @RequestParam(value = "condition", required = false) Integer condition,
	        @RequestParam(value = "managerIdx", required = false) Long managerIdx,
	        @RequestParam(value = "startDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date startDatetime,
            @RequestParam(value = "endDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date endDatetime,
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
	    
	    System.out.println("Received content: " + content);
	    
	    Project project = projectOpt.get();
	    
	    switch (tabType) {
	        case 1: // paragraph
	            postService.createParagraph(member, project, title, content, openRange);
	            break;
	        case 2: // task
	        	// test
	        	managerIdx = 1l;
	        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	        	try {
	                endDate = formatter.parse("2024/07/08");
	                System.out.println(endDate);
	            } catch (ParseException e) {
	                System.out.println("Invalid date format: " + e.getMessage());
	            }
	            if (managerIdx == null || !memberRepository.findById(managerIdx).isPresent()) {
	                // Handle manager not found
	                return "redirect:/error";
	            }
	            Member manager = memberRepository.findById(managerIdx).get();
	            postService.createTask(member, project, title, condition, manager, endDate, content, lowerTaskNameList, lowerTaskConditionList);
	            break;
	        case 3: // schedule
	        	Date finalStartDate = startDatetime != null ? startDatetime : startDate;
	            Date finalEndDate = endDatetime != null ? endDatetime : endDate;
	            // 회사멤버가 아닌 프로젝트 멤버로 수정할것!!!!! - 우태균 정신 차려
	        	scheduleAttenderList = memberRepository.findByCompanyCompanyIdx(member.getCompany().getCompanyIdx());
	            postService.createSchedule(member, project, title, finalStartDate, finalEndDate, place, content, scheduleAttenderList);
	            break;
	        case 4: // todo
	            postService.createTodo(member, project, title, todoNameList, todoManagerList, todoDeadlineList);
	            break;
	        case 5: // vote
	        	Date currentDate = new Date();

	            // Calendar 객체를 사용하여 하루를 더합니다.
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(currentDate);
	            calendar.add(Calendar.DAY_OF_YEAR, 1);
	        	voteEnddate = calendar.getTime();
	            postService.createVote(member, project, title, voteDetail, voteEnddate, voteItemList);
	            break;
	        default:
	            // Handle invalid tabType
	            return "redirect:/error";
	    }
	    return "redirect:/projectFeed2/" + projectIdx;
	}

}

