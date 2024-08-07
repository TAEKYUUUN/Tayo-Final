package com.mysite.tayo.controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.service.CommentService;
import com.mysite.tayo.service.MemberService;
import com.mysite.tayo.service.PostService;
import com.mysite.tayo.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProjectController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectMemberRepository projectMemberRepository;

	@Autowired
	private PostRepository postRepository;

	@GetMapping("/projectFeed/{projectIdx}")
	public String feed(Model model, Authentication authentication, @PathVariable("projectIdx") Long projectIdx) {
		Member member = memberService.infoFromLogin(authentication);
		Optional<Project> project = projectRepository.findById(projectIdx);
		List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectProjectIdx(projectIdx);
		Optional<ProjectMember> projectMember = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdx(projectIdx, member.getMemberIdx());
		List<Post> postList = postRepository.findAllByProjectProjectIdxOrderByUploadDateDesc(projectIdx);
		List<Member> companyMemberNotInProject = projectService.findMembersNotInProject(projectIdx,
				member.getCompany().getCompanyIdx());
		List<Map<String, Object>> postsData = new ArrayList<>();

		for (Post post : postList) {
			Map<String, Object> postData = new HashMap<>();
			postData.put("post", post);
			postData.put("member", post.getMember()); // 포스트 작성자 member 객체
			
			// 댓글을 작성 시간 기준으로 정렬 및 좋아요 수 포함
			Map<Comments, Integer> commentsWithLikeCount = commentService.getCommentsWithLikeCount(post, member);
	        List<Map.Entry<Comments, Integer>> sortedEntries = new ArrayList<>(commentsWithLikeCount.entrySet());
	        sortedEntries.sort(Comparator.comparing(entry -> entry.getKey().getWriteTime()));

	        List<Map<String, Object>> commentsData = new ArrayList<>();
	        for (Map.Entry<Comments, Integer> entry : sortedEntries) {
	            Map<String, Object> commentData = new HashMap<>();
	            commentData.put("comment", entry.getKey());
	            commentData.put("likeCount", entry.getValue());
	            commentData.put("isLiked", entry.getKey().isLiked());
	            commentsData.add(commentData);
	        }
	        postData.put("comments", commentsData);
			
			switch (post.getFileType()) {
			case 1:
				Map<String, String> paragraphData = postService.getParagraph(post.getPostIdx());
				if (paragraphData != null) {
					postData.put("data", paragraphData);
				} else {
					postData.put("data", Collections.emptyMap()); // 빈 맵을 설정하여 NullPointerException 방지
				}
				break;
			case 2:
				Map<String, Object> taskData = postService.getTask(post.getPostIdx());
				if (taskData != null) {
					postData.put("data", taskData);
				} else {
					postData.put("data", Collections.emptyMap());
				}
				break;
			case 3:
				Map<String, Object> scheduleData = postService.getSchedule(post.getPostIdx());
				if (scheduleData != null) {
					postData.put("data", scheduleData);
					
					// 참석 상태 카운트 계산
	                List<Map<String, String>> attenders = (List<Map<String, String>>) scheduleData.get("attenders");
	                if (attenders != null) {
	                    long participateCount = attenders.stream()
	                        .filter(a -> "1".equals(a.get("isAttend")))
	                        .count();
	                    long absenceCount = attenders.stream()
	                        .filter(a -> "2".equals(a.get("isAttend")))
	                        .count();
	                    long undetermineCount = attenders.stream()
	                        .filter(a -> "3".equals(a.get("isAttend")))
	                        .count();

	                    Map<String, Long> attenderCounts = new HashMap<>();
	                    attenderCounts.put("participate", participateCount);
	                    attenderCounts.put("absence", absenceCount);
	                    attenderCounts.put("undetermine", undetermineCount);

	                    postData.put("attenderCounts", attenderCounts);

	                    // 로그인한 사용자의 참석 상태 가져오기
	                    Optional<Map<String, String>> currentAttenderOpt = attenders.stream()
	                            .filter(a -> member.getMemberIdx().toString().equals(a.get("memberIdx"))).findFirst();
	                    if (currentAttenderOpt.isPresent()) {
	                        Map<String, String> currentAttender = currentAttenderOpt.get();
	                        postData.put("currentAttenderStatus", Integer.parseInt(currentAttender.get("isAttend")));
	                    } else {
	                        postData.put("currentAttenderStatus", 0); // 기본값: 참석 상태 없음
	                    }
	                } else {
	                    postData.put("attenderCounts", Collections.emptyMap());
	                    postData.put("currentAttenderStatus", 0); // 기본값: 참석 상태 없음
	                }
				} else {
					postData.put("data", Collections.emptyMap());
				}
				break;
			case 4:
				Map<String, Object> todoData = postService.getTodo(post.getPostIdx(), member.getMemberIdx());
				if (todoData != null) {
					postData.put("data", todoData);
				} else {
					postData.put("data", Collections.emptyMap());
				}
				break;
			case 5:
				Map<String, Object> voteData = postService.getVote(post.getPostIdx());
				if (voteData != null) {
					postData.put("data", voteData);
				} else {
					postData.put("data", Collections.emptyMap());
				}
				break;
			}
			postsData.add(postData);
		}
		// projectMemberList 정렬
		projectMemberList.sort(Comparator.comparing(pm -> pm.getMember().getName(), Collator.getInstance()));

		// companyMemberNotInProject 정렬
		companyMemberNotInProject.sort(Comparator.comparing(Member::getName, Collator.getInstance()));

		model.addAttribute("postsData", postsData);
		model.addAttribute("member", member);
		model.addAttribute("projectMember", projectMember.orElse(null));
		model.addAttribute("projectMemberList", projectMemberList);
		model.addAttribute("project", project.orElse(null));
		model.addAttribute("postList", postList);
		model.addAttribute("companyMemberList", companyMemberNotInProject);

		return "projectFeed";
	}

	// 프로젝트 생성 페이지 이동
	@GetMapping("/createNewProject")
	public String createNewProject() {
		return "createNewProject";
	}

	@PostMapping("/createNewProject")
	public String createProject(@RequestParam("projectName") String projectName,
			@RequestParam("main_tab") Integer mainTab,
			@RequestParam(value = "companyProject", defaultValue = "0") Integer projectType,
			@RequestParam(value = "needConfirm", defaultValue = "0") Integer withoutConfirm,
			Authentication authentication) {
		Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
		Company company = memberService.infoFromLogin(authentication).getCompany();
		projectService.createProject(projectName, mainTab, projectType, withoutConfirm, memberIdx, company);

		return "redirect:/dashboard";
	}

	@GetMapping("/projectlist")
	public String getMyprojectList(Model model, Authentication authentication) {
		Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
		List<ProjectMember> projectList = this.projectService.getMyProject(memberIdx);
		List<Long> countMember = this.projectService.getCountProjectMember(projectList);
		model.addAttribute("projectList", projectList);
		model.addAttribute("countMember", countMember);
		return "myProject";
	}

	// 즐찾 설정&해제
	@PostMapping("/projectlist")
	public String updateHotlist(@RequestParam(value = "isHotlist", defaultValue = "") String isHotlist,
			@RequestParam(name = "projectIdx") Long projectIdx, Authentication authentication) {
		Long memberIdx = memberService.infoFromLogin(authentication).getMemberIdx();
		Optional<ProjectMember> optionalMember = this.projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdx(projectIdx, memberIdx);
		if (optionalMember.isPresent()) {
			ProjectMember member = optionalMember.get();
			if ("1".equals(isHotlist)) {
				member.setHotlist(null);
			} else {
				member.setHotlist(1);
			}
			projectMemberRepository.save(member);
		}
		return "redirect:/projectlist";
	}

	@GetMapping("/companyOpenProject")
	public String companyOpenProejct() {
		return "companyOpenProject";
	}

	// 프로젝트 멤버 추가
	@PostMapping("/inviteParticipants/{projectIdx}")
	public String inviteParticipants(@RequestBody List<Member> members, @PathVariable("projectIdx") Long projectIdx) {
		projectService.addProjectMembers(members, projectIdx);

		return "redirect:/projectFeed";
	}
	
}
