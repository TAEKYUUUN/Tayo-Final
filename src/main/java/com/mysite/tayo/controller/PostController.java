package com.mysite.tayo.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.LowerTask;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Paragraph;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.entity.Schedule;
import com.mysite.tayo.entity.Task;
import com.mysite.tayo.entity.Todo;
import com.mysite.tayo.entity.TodoName;
import com.mysite.tayo.entity.Vote;
import com.mysite.tayo.entity.VoteItem;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.CommentService;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.PostMemberService;
import com.mysite.tayo.service.PostService;
import com.mysite.tayo.service.ProjectService;

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
	
	@Autowired
	private PostMemberService postMemberService;
	
	@Autowired
	private ProjectService projectService;
	
	
	// 프로젝트 내 게시글 작성 PostMapping
	@PostMapping("/projectFeed/{projectIdx}")
	public String createPost(Authentication authentication, @PathVariable("projectIdx") Long projectIdx,
			@RequestParam("tabType") int tabType, @RequestParam("title") String title,
			@RequestParam("openRange") int openRange, @RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "condition", required = false) Integer condition,
			@RequestParam(value = "selectedDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date allEndDate,
			@RequestParam(value = "startDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date startDatetime,
			@RequestParam(value = "endDatetime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date endDatetime,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "place", required = false) String place,
			@RequestParam(value = "voteDetail", required = false) String voteDetail,
			@RequestParam(value = "lowerTaskNames", required = false) List<String> lowerTaskNameList,
			@RequestParam(value = "lowerTaskConditions", required = false) List<Integer> lowerTaskConditionList,
			@RequestParam(value = "place_id", required = false) String placeId,
			@RequestParam(value = "place_lat", required = false) Double placeLat,
			@RequestParam(value = "place_lng", required = false) Double placeLng,
			@RequestParam(value = "deadline", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadLine,
			@RequestParam(value = "managerIdx", required = false) Long managerIdx,
			@RequestParam(value = "todoNames", required = false) List<String> todoNameList,
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
			if (managerIdx == null || !memberRepository.findById(managerIdx).isPresent()) {
				// Handle manager not found
				return "redirect:/error";
			}
			Member taskManager = memberRepository.findById(managerIdx).get();
			postService.createTask(member, project, title, condition, taskManager, allEndDate, content,
					lowerTaskNameList, lowerTaskConditionList);
			break;
		case 3: // schedule
			Date finalStartDate = startDatetime != null ? startDatetime : startDate;
			Date finalEndDate = endDatetime != null ? endDatetime : endDate;

			List<ProjectMember> scheduleAttenderList = projectService.getAllProjectMember(project.getProjectIdx());
			postService.createSchedule(member, project, title, finalStartDate, finalEndDate, place, placeId, placeLat,
					placeLng, content, scheduleAttenderList);
			break;
		case 4: // todo
			if (managerIdx == null || !memberRepository.findById(managerIdx).isPresent()) {
				// Handle manager not found
				return "redirect:/error";
			}
			Member todoManager = memberRepository.findById(managerIdx).get();
			postService.createTodo(member, project, title, todoManager, allEndDate, todoNameList);
			break;
		case 5: // vote
			// 만약 마감일 추가 안할 시 , 기본 작성일 기준 +3일 마감일으로 적용.
			if(allEndDate == null) {
				Calendar calendar = Calendar.getInstance();
	            calendar.add(Calendar.DAY_OF_YEAR, 3);
	            allEndDate = calendar.getTime();
			}
			postService.createVote(member, project, title, voteDetail, allEndDate, voteItemList, isPlural, isAnonymous);
			break;
		default:
			// Handle invalid tabType
			return "redirect:/error";
		}
		return "redirect:/projectFeed/" + projectIdx;
	}
	
	
	// 댓글 작성 PostMapping
	@PostMapping("/comments")
	public @ResponseBody ResponseEntity<Map<String, Object>> addComment(@RequestBody Map<String, Object> payload,
	        Authentication authentication) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        Member member = memberService.infoFromLogin(authentication);
	        Long postIdx = Long.valueOf(payload.get("postIdx").toString());
	        String contents = payload.get("contents").toString();

	        Optional<Post> postOptional = postService.getPost(postIdx);

	        if (!postOptional.isPresent()) {
	            response.put("success", false);
	            response.put("message", "Post not found");
	            System.out.println("Response: " + response); // 디버깅용 로그 추가
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        Post post = postOptional.get();
	        commentService.createComment(member, post, contents);
	        response.put("success", true);
	        response.put("message", "댓글이 등록되었습니다.");
	        System.out.println("Response: " + response); // 디버깅용 로그 추가
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "댓글 등록에 실패했습니다.");
	        System.out.println("Error Response: " + response); // 디버깅용 로그 추가
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
	// 해당 게시글의 댓글들 가져오는 GetMapping
	@GetMapping("/comments")
	public @ResponseBody ResponseEntity<Map<String, Object>> getComments(@RequestParam("postIdx") Long postIdx) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Post> postOptional = postService.getPost(postIdx);

			if (!postOptional.isPresent()) {
				response.put("success", false);
				response.put("message", "Post not found");
				System.out.println("Response: " + response); // 디버깅용 로그 추가
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			Post post = postOptional.get();
			List<Comments> comments = commentService.findByPost(post);
			response.put("success", true);
			response.put("comments", comments);
			System.out.println("Response: " + response); // 디버깅용 로그 추가
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "댓글을 불러오는데 실패했습니다.");
			System.out.println("Error Response: " + response); // 디버깅용 로그 추가
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 댓글 삭제하는 DeleteMapping
	@DeleteMapping("/comments/{commentIdx}/delete")
    public @ResponseBody ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentIdx") Long commentIdx) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = commentService.deleteComment(commentIdx);
            if (deleted) {
                response.put("success", true);
                response.put("message", "댓글이 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "댓글 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
	// 게시물 삭제하는 DeleteMapping
	@DeleteMapping("/{postIdx}/delete")
	public @ResponseBody Map<String, Object> deletePost(@PathVariable("postIdx") Long postIdx) {
	    postService.deletePostById(postIdx);

	    Map<String, Object> response = new HashMap<>();
	    response.put("success", true);
	    
	    return response;
	}
	
	
	// 댓글 좋아요 표시 PostMapping
	@PostMapping("/checkCommentLike/{commentIdx}")
	public @ResponseBody ResponseEntity<Map<String, Object>> checkCommentLike(@PathVariable("commentIdx") Long commentIdx, Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);
	    System.out.println("checkCommentLike called with commentIdx: " + commentIdx + " and member: " + member.getEmail());

	    try {
	        commentService.checkCommentLike(commentIdx, member);
	        int newLikeCount = commentService.getLikeCount(commentIdx);
	        System.out.println("New like count for comment " + commentIdx + ": " + newLikeCount);

	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("newLikeCount", newLikeCount);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace(); // 에러 로그 출력
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", false);
	        response.put("error", e.getMessage()); // 에러 메시지 추가
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	// 댓글 좋아요 취소 DeleteMapping
	@DeleteMapping("/cancelCommentLike/{commentIdx}")
	public @ResponseBody Map<String, Object> deleteCommentsReact(@PathVariable("commentIdx") Long commentIdx, Authentication authentication) {
	    Member member = memberService.infoFromLogin(authentication);
	    commentService.cancelCommentLike(commentIdx, member.getMemberIdx());

	    Map<String, Object> response = new HashMap<>();
	    response.put("success", true);

	    return response;
	}
	
	// Task(업무) 진행상태 업데이트 PostMapping
	@PostMapping("/updateTaskCondition")
	public @ResponseBody ResponseEntity<Map<String, String>> updateTaskCondition(@RequestBody Map<String, Object> payload, Authentication authentication) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        Member member = memberService.infoFromLogin(authentication);
	        Long memberIdx = member.getMemberIdx();

	        // Retrieve and convert values from the payload
	        Long postIdx = Long.valueOf((Integer) payload.get("postIdx"));
	        int newCondition = (Integer) payload.get("newCondition");

	        postMemberService.updateTaskCondition(postIdx, newCondition, memberIdx);

	        response.put("message", "업무 상태가 정상적으로 변경되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("message", "권한이 없습니다.");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }
	}
	
	// Schedule(일정) 참여여부 Update PostMapping
	@PostMapping("/checkScheduleAttendance")
    public @ResponseBody ResponseEntity<Map<String, String>> checkScheduleAttendance(
            @RequestParam("postIdx") Long postIdx,
            @RequestParam("attendance") int attendance,
            Authentication authentication) {
        
        Member member = memberService.infoFromLogin(authentication);
        Long memberIdx = member.getMemberIdx();
        Map<String, String> response = new HashMap<>();
        
        try {
            postMemberService.checkScheduleAttendance(postIdx, memberIdx, attendance);
            response.put("message", "일정 참여여부가 정상적으로 변경되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "참여여부 업데이트에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
	// 할일 체크 통해 Update PutMapping
	@PutMapping("/todo/update/{todonameIdx}")
	public @ResponseBody ResponseEntity<Map<String, Object>> updateTodoMemberStatus(
	        Authentication authentication,
	        @PathVariable("todonameIdx") Long todonameIdx,
	        @RequestParam("isDone") Integer isDone) {

	    Member member = memberService.infoFromLogin(authentication);
	    Map<String, Object> response = new HashMap<>();

	    // 현재 로그인된 멤버가 해당 할 일 항목의 멤버인지 확인
	    Long memberIdx = member.getMemberIdx();
	    boolean updateSuccess = postMemberService.updateTodoMemberStatus(todonameIdx, memberIdx, isDone);
	    
	    if (updateSuccess) {
	        response.put("success", true);
	        response.put("message", "Todo status updated successfully.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("success", false);
	        response.put("message", "Failed to update todo status.");
	        return ResponseEntity.status(403).body(response); // 권한 없음
	    }
	}
	
	
	// 투표 참여 PostMapping
	@PostMapping("/vote/{postIdx}")
	public ResponseEntity<?> vote(@PathVariable("postIdx") Long postIdx,
	                              @RequestBody Map<String, Object> request,
	                              Authentication authentication) {
	    try {
	        Member member = memberService.infoFromLogin(authentication);
	        Long memberIdx = member.getMemberIdx();
	        List<?> voteItemIdsRaw = (List<?>) request.get("voteItemIds");
	        List<Long> voteItemIdxs = voteItemIdsRaw.stream()
	                                                .map(id -> Long.valueOf(id.toString()))
	                                                .collect(Collectors.toList());

	        postMemberService.participateVote(postIdx, memberIdx, voteItemIdxs);

	        return ResponseEntity.ok().body(Collections.singletonMap("message", "Vote submitted successfully"));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
	    }
	}
	
	//투표종료 PostMapping
	@PostMapping("vote/end/{postIdx}")
    public ResponseEntity<?> endVote(@PathVariable("postIdx") Long postIdx) {
        try {
            postMemberService.endVote(postIdx);
            return ResponseEntity.ok("Vote ended successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
	
	// 투표 결과 받아오는 GetMapping
	@GetMapping("vote/{postIdx}/results")
    public ResponseEntity<?> getVoteResults(@PathVariable("postIdx") Long postIdx) {
        try {
            Map<String, Integer> results = postMemberService.getVoteResults(postIdx);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
	
	
	// 글 수정할 데이터를 받아오는 GetMapping
	@GetMapping("/projectFeed/{projectIdx}/edit/{postIdx}")
	public @ResponseBody ResponseEntity<Map<String, Object>> getPostForEdit(@PathVariable("projectIdx") Long projectIdx,
			@PathVariable("postIdx") Long postIdx) {
		Optional<Post> postOptional = postService.getPost(postIdx);
		if (!postOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
		}

		Post post = postOptional.get();
		Map<String, Object> postData = new HashMap<>();
		postData.put("fileType", post.getFileType());

		// 기타 필요한 데이터 추가
		switch (post.getFileType()) {
		case 1: // Paragraph
			Optional<Paragraph> paragraphOptional = postService.getParagraphByPostIdx(postIdx);
			if (paragraphOptional.isPresent()) {
				Paragraph paragraph = paragraphOptional.get();
				postData.put("title", paragraph.getTitle());
				postData.put("content", paragraph.getContents());
			}
			break;
		case 2: // Task
			Optional<Task> taskOptional = postService.getTaskByPostIdx(postIdx);
			if (taskOptional.isPresent()) {
				Task task = taskOptional.get();
				postData.put("taskName", task.getTaskName());
				postData.put("condition", task.getCondition());
				postData.put("managerName", task.getManager().getName());
				postData.put("managerIdx", task.getManager().getMemberIdx());
				postData.put("taskEndDate", task.getEndDate());
				postData.put("content", task.getContents());
				// 하위업무 들
				List<Map<String, Object>> lowerTasksData = new ArrayList<>();
				List<LowerTask> lowerTasks = task.getLowerTasks();
				for (LowerTask lowerTask : lowerTasks) {
					Map<String, Object> lowerTaskData = new HashMap<>();
					lowerTaskData.put("lowerTaskName", lowerTask.getTaskName());
					lowerTaskData.put("lowerTaskCondition", lowerTask.getCondition());
					lowerTasksData.add(lowerTaskData);
				}
				postData.put("lowerTasks", lowerTasksData);
			}
			break;
		case 3: // Schedule
			Optional<Schedule> scheduleOptional = postService.getScheduleByPostIdx(postIdx);
			if (scheduleOptional.isPresent()) {
				Schedule schedule = scheduleOptional.get();
				postData.put("title", schedule.getTitle());
				postData.put("startDatetime", schedule.getStartDate());
				postData.put("endDatetime", schedule.getEndDate());
				postData.put("place", schedule.getPlace());
				postData.put("place_id", schedule.getPlaceId());
				postData.put("place_lat", schedule.getPlaceLat());
				postData.put("place_lng", schedule.getPlaceLng());
				postData.put("content", schedule.getContents());
			}
			break;
		case 4: // Todo
			Optional<Todo> todoOptional = postService.getTodoByPostIdx(postIdx);
			if (todoOptional.isPresent()) {
				Todo todo = todoOptional.get();
				postData.put("title", todo.getTitle());
				postData.put("managerIdx", todo.getTodoManager().getMemberIdx());
				postData.put("managerName", todo.getTodoManager().getName());
				postData.put("deadLine", todo.getDeadline());
				// 할일 들
				List<Map<String, Object>> todoNamesData = new ArrayList<>();
				List<TodoName> todoNames = todo.getTodoNames();
				for (TodoName todoName : todoNames) {
					Map<String, Object> todoNameData = new HashMap<>();
					todoNameData.put("todoName", todoName.getTodoName());
					todoNameData.put("isFinished", todoName.getIsFinished());
					todoNamesData.add(todoNameData);
				}
				postData.put("todoNames", todoNamesData);
			}
			break;
		case 5: // Vote
			Optional<Vote> voteOptional = postService.getVoteByPostIdx(postIdx);
			if (voteOptional.isPresent()) {
				Vote vote = voteOptional.get();
				postData.put("title", vote.getTitle());
				postData.put("voteDetail", vote.getVoteDetail());
				postData.put("voteEnddate", vote.getVoteEndDate());
				postData.put("isplural", vote.getIsPlural());
				postData.put("isanonymous", vote.getIsAnonymous());
				// 투표항목들
				List<Map<String, Object>> voteItemsData = new ArrayList<>();
				List<VoteItem> voteItems = vote.getVoteItems();
				for (VoteItem voteItem : voteItems) {
					Map<String, Object> voteItemData = new HashMap<>();
					voteItemData.put("itemName", voteItem.getItemName());
					voteItemsData.add(voteItemData);
				}
				postData.put("voteItems", voteItemsData);
			}
			break;
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyMap());
		}

		return ResponseEntity.ok(postData);
	}
	
	// 수정요청을 처리하는 PutMapping
	@PutMapping("/projectFeed/{projectIdx}/edit/{postIdx}")
	public ResponseEntity<Map<String, Object>> updatePost(Authentication authentication,
	        @PathVariable("projectIdx") Long projectIdx, @PathVariable("postIdx") Long postIdx,
	        @RequestBody Map<String, Object> allParams) {
	    // Log all received parameters for debugging
	    System.out.println("Received parameters for post update:");
	    allParams.forEach((key, value) -> System.out.println(key + ": " + value));

	    try {
	        Map<String, Object> params = new HashMap<>(allParams);

	        // Convert specific parameters to required types with null checks
	        if (allParams.containsKey("condition")) {
	            params.put("condition", Integer.parseInt(allParams.get("condition").toString()));
	        }
	        if (allParams.containsKey("managerIdx")) {
	            params.put("managerIdx", Long.parseLong(allParams.get("managerIdx").toString()));
	        }
	        if (allParams.containsKey("selectedDate") && !allParams.get("selectedDate").toString().isEmpty()) {
	            params.put("selectedDate", java.sql.Date.valueOf(allParams.get("selectedDate").toString()));
	        }

	        if (allParams.containsKey("startDatetime")) {
	            params.put("startDatetime", new Date(Long.parseLong(allParams.get("startDatetime").toString())));
	        }
	        if (allParams.containsKey("endDatetime")) {
	            params.put("endDatetime", new Date(Long.parseLong(allParams.get("endDatetime").toString())));
	        }

	        List<String> lowerTaskConditions = (List<String>) allParams.get("lowerTaskConditions");
	        List<String> lowerTaskNames = (List<String>) allParams.get("lowerTaskNames");

	        if (lowerTaskConditions != null) {
	            params.put("lowerTaskConditions", lowerTaskConditions);
	        }
	        if (lowerTaskNames != null) {
	            params.put("lowerTaskNames", lowerTaskNames);
	        }

	        if (allParams.containsKey("place")) {
	            params.put("place", allParams.get("place").toString());
	        }
	        if (allParams.containsKey("place_id")) {
	            params.put("place_id", allParams.get("place_id").toString());
	        }
	        if (allParams.containsKey("place_lat")) {
	            params.put("place_lat", Double.parseDouble(allParams.get("place_lat").toString()));
	        }
	        if (allParams.containsKey("place_lng")) {
	            params.put("place_lng", Double.parseDouble(allParams.get("place_lng").toString()));
	        }
	        if (allParams.containsKey("deadLine") && !allParams.get("deadLine").toString().isEmpty()) {
	            params.put("deadLine", java.sql.Date.valueOf(allParams.get("deadLine").toString()));
	        }

	        // Handle tabType specific fields
	        String tabType = allParams.get("tabType").toString();
	        switch (tabType) {
	            case "1":
	                if (!params.containsKey("title") || !params.containsKey("content")) {
	                    throw new IllegalArgumentException("Title and content are required for tabType 1");
	                }
	                break;
	            case "2":
	                if (!params.containsKey("title") || !params.containsKey("content") ||
	                    !params.containsKey("condition") || !params.containsKey("managerIdx") ||
	                    !params.containsKey("selectedDate")) {
	                    throw new IllegalArgumentException("Title, content, condition, managerIdx, and selectedDate are required for tabType 2");
	                }
	                break;
	            case "3":
	                if (!params.containsKey("title") || !params.containsKey("startDatetime") ||
	                    !params.containsKey("endDatetime") || !params.containsKey("place")) {
	                    throw new IllegalArgumentException("Title, startDatetime, endDatetime, and place are required for tabType 3");
	                }
	                break;
	            case "4":
	                if (!params.containsKey("title") || !params.containsKey("managerIdx") ||
	                    !params.containsKey("deadLine") || !params.containsKey("todoNames")) {
	                    throw new IllegalArgumentException("Title, managerIdx, deadLine, and todoNames are required for tabType 4");
	                }
	                break;
	            case "5":
	                if (!params.containsKey("title") || !params.containsKey("voteDetail") ||
	                    !params.containsKey("voteEnddate") || !params.containsKey("isplural") ||
	                    !params.containsKey("isanonymous") || !params.containsKey("voteItems")) {
	                    throw new IllegalArgumentException("Title, voteDetail, voteEnddate, isplural, isanonymous, and voteItems are required for tabType 5");
	                }
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid tabType: " + tabType);
	        }

	        System.out.println("Converted parameters:");
	        params.forEach((key, value) -> System.out.println(key + ": " + value));

	        postService.updatePost(postIdx, params);

	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Post updated successfully.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", false);
	        response.put("message", "Failed to update post: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}
