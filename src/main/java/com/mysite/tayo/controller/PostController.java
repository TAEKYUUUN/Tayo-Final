package com.mysite.tayo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.CommentService;
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
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@PostMapping("/projectFeed/{projectIdx}")
	public String createPost(Authentication authentication, @PathVariable("projectIdx") Long projectIdx,
			@RequestParam("tabType") int tabType, @RequestParam("title") String title,
			@RequestParam("openRange") int openRange, @RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "condition", required = false) Integer condition,
			@RequestParam(value = "selectedDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date taskEndDate,
			@RequestParam(value = "startDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date startDatetime,
			@RequestParam(value = "endDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date endDatetime,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "place", required = false) String place,
			@RequestParam(value = "voteDetail", required = false) String voteDetail,
			@RequestParam(value = "lowerTaskNames", required = false) List<String> lowerTaskNameList,
			@RequestParam(value = "lowerTaskConditions", required = false) List<Integer> lowerTaskConditionList,
			@RequestParam(value = "scheduleAttenders", required = false) List<Member> scheduleAttenderList,
			@RequestParam(value = "place_id", required = false) String placeId,
			@RequestParam(value = "place_lat", required = false) Double placeLat,
			@RequestParam(value = "place_lng", required = false) Double placeLng,
			@RequestParam(value = "deadline", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadLine,
			@RequestParam(value = "managerIdx", required = false) Long managerIdx,
			@RequestParam(value = "todoNames", required = false) List<String> todoNameList,
			@RequestParam(value = "voteEnddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date voteEnddate,
			@RequestParam(value = "voteItems", required = false) List<String> voteItemList,
			@RequestParam(value = "isplural", required = false) Integer isPlural,
			@RequestParam(value = "isanonymous", required = false) Integer isAnonymous) {
		Member member = memberService.infoFromLogin(authentication);
		Optional<Project> projectOpt = projectRepository.findById(projectIdx);

		if (!projectOpt.isPresent()) {
			// Handle project not found
			return "redirect:/error";
		}

		System.out.println("Received content: " + content);

		Project project = projectOpt.get();
		System.out.println(tabType);
		switch (tabType) {
		case 1: // paragraph
			postService.createParagraph(member, project, title, content, openRange);
			break;
		case 2: // task
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
			Member taskManager = memberRepository.findById(managerIdx).get();
			postService.createTask(member, project, title, condition, taskManager, taskEndDate, content, lowerTaskNameList,
					lowerTaskConditionList);
			break;
		case 3: // schedule
			Date finalStartDate = startDatetime != null ? startDatetime : startDate;
			Date finalEndDate = endDatetime != null ? endDatetime : endDate;
			// 회사멤버가 아닌 프로젝트 멤버로 수정할것!!!!! - 우태균 정신 차려
			scheduleAttenderList = memberRepository.findByCompanyCompanyIdx(member.getCompany().getCompanyIdx());
			postService.createSchedule(member, project, title, finalStartDate, finalEndDate,
					place, placeId, placeLat, placeLng, content, scheduleAttenderList);
			break;
		case 4: // todo
			if (managerIdx == null || !memberRepository.findById(managerIdx).isPresent()) {
				// Handle manager not found
				return "redirect:/error";
			}
			Member todoManager = memberRepository.findById(managerIdx).get();
			postService.createTodo(member, project, title, todoManager, deadLine, todoNameList);
			break;
		case 5: // vote
			Date currentDate = new Date();

			// Calendar 객체를 사용하여 하루를 더합니다.
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			voteEnddate = calendar.getTime();
			postService.createVote(member, project, title, voteDetail, voteEnddate, voteItemList, isPlural,
					isAnonymous);
			break;
		default:
			// Handle invalid tabType
			return "redirect:/error";
		}
		return "redirect:/projectFeed/" + projectIdx;
	}

	@PostMapping("/comments")
	public @ResponseBody ResponseEntity<Map<String, Object>> addComment(@RequestBody Comments comment,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		try {
			Member member = memberService.infoFromLogin(authentication);
			Optional<Post> postOptional = postService.getPostByComment(comment);

			if (!postOptional.isPresent()) {
				response.put("success", false);
				response.put("message", "Post not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			Post post = postOptional.get();
			commentService.createComment(member, post, comment.getContents());
			response.put("success", true);
			response.put("message", "댓글이 등록되었습니다.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "댓글 등록에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@DeleteMapping("/{postIdx}")
	public @ResponseBody String deletePost(@PathVariable("postIdx") Long postIdx) {
		postService.deletePostById(postIdx);
		return "sex";
	}
	
}
