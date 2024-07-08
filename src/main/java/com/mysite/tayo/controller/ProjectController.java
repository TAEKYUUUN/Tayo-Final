package com.mysite.tayo.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.repository.ParagraphRepository;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ProjectRepository;
import com.mysite.tayo.repository.ScheduleRepository;
import com.mysite.tayo.repository.TaskRepository;
import com.mysite.tayo.repository.TodoRepository;
import com.mysite.tayo.repository.VoteRepository;
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
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private VoteRepository voteRepository;

    // 테스트용
    @GetMapping("/projectFeed/{projectIdx}")
    public String feed(Model model, Authentication authentication, @PathVariable("projectIdx") Long projectIdx) {
        Member member = memberService.infoFromLogin(authentication);
        Optional<Project> project = projectRepository.findById(projectIdx);
        List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectProjectIdx(projectIdx);
        Optional<ProjectMember> projectMember = projectMemberRepository
                .findByProjectProjectIdxAndMemberMemberIdx(projectIdx, member.getMemberIdx());
        List<Post> postList = postRepository.findAllByProjectProjectIdxOrderByUploadDateDesc(projectIdx);
        List<Member> companyMemberAll = memberService.getListByCompanyIdx(member.getCompany().getCompanyIdx());
        List<Map<String, Object>> postsData = new ArrayList<>();

        for (Post post : postList) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("post", post);
            postData.put("member", post.getMember()); // 포스트 작성자 member 객체

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
                        postData.put("manager", taskData.get("manager"));
                    } else {
                        postData.put("data", Collections.emptyMap());
                    }
                    break;
                case 3:
                    Map<String, Object> scheduleData = postService.getSchedule(post.getPostIdx());
                    if (scheduleData != null) {
                        postData.put("data", scheduleData);
                    } else {
                        postData.put("data", Collections.emptyMap());
                    }
                    break;
                case 4:
                    Map<String, Object> todoData = postService.getTodo(post.getPostIdx());
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

        model.addAttribute("postsData", postsData);
        model.addAttribute("member", member);
        model.addAttribute("projectMember", projectMember.orElse(null));
        model.addAttribute("projectMemberList", projectMemberList);
        model.addAttribute("project", project.orElse(null));
        model.addAttribute("postList", postList);
        model.addAttribute("companyMemberList", companyMemberAll);

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

}
