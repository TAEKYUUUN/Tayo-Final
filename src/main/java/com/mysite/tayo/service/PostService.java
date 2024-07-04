package com.mysite.tayo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.LowerTask;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Paragraph;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.PostMember;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.entity.Schedule;
import com.mysite.tayo.entity.ScheduleAttender;
import com.mysite.tayo.entity.Task;
import com.mysite.tayo.entity.Todo;
import com.mysite.tayo.entity.TodoMember;
import com.mysite.tayo.entity.TodoName;
import com.mysite.tayo.entity.Vote;
import com.mysite.tayo.entity.VoteItem;
import com.mysite.tayo.entity.Voter;
import com.mysite.tayo.repository.LowerTaskRepository;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.ParagraphRepository;
import com.mysite.tayo.repository.PostMemberRepository;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final ParagraphRepository paragraphRepositroy;
	private final TaskRepository taskRepository;
	private final PostMemberRepository postMemberRepository;
	private final ScheduleRepository scheduleRepository;
	private final TodoRepository todoRepository;
	private final VoteRepository voteRepository;
	private final LowerTaskRepository lowerTaskRepository;
	private final ScheduleAttenderRepository scheduleAttenderRepository;
	private final TodoNameRepository todoNameRepository;
	private final TodoMemberRepository todoMemberRepository;
	private final VoteItemRepository voteItemRepository;
	private final VoterRepository voterRepository;
	
	// 글(Paragraph) 생성
	public void createParagraph(Member member, Project project, String title, String contents, int openRange) {
		Date date = new Date();

		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(1);
		this.postRepository.save(post);
		
		Paragraph paragraph = new Paragraph();
		paragraph.setPost(post);
		paragraph.setTitle(title);
		paragraph.setContents(contents);
		paragraph.setOpenRange(openRange);
		this.paragraphRepositroy.save(paragraph);
		
		// post_member 에 프로젝트 참여중인 모든 멤버 추가 (작성자 포함)
		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());
		for(ProjectMember projectMember : projectMemberAll) {
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
			
		}
		// uncheck_post 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		
		// alarm 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		
		// unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
	}
	
	// 업무(Task) 생성
	public void createTask(Member member, Project project, String taskName, int condition,
			Long managerIdx, Date endDate, String contents, int existLowerTask) {
		Date date = new Date();
		
		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(2);
		this.postRepository.save(post);
		
		Task task = new Task();
		task.setPost(post);
		task.setTaskName(taskName);
		task.setCondition(condition);
		task.setMember(member);
		task.setStartDate(date);
		task.setEndDate(endDate);
		task.setUploadDate(date);
		task.setContents(contents);
		this.taskRepository.save(task);
		
		// int existLowerTask -> hidden input 생성해서 , 하위 업무 있을 떄는 1/ 없을 떄는 0을 반환하게끔 추가
		if(existLowerTask != 0) {
			LowerTask lowerTask = new LowerTask();
			
		}
	}
	
	// 일정(Schedule) 생성
	public void createSchedule() {
		Date date = new Date();
		
		Post post = new Post();
		
		Schedule schedule = new Schedule();
		
		ScheduleAttender scheduleAttender = new ScheduleAttender();
	}
	
	// 할일(Todo) 생성
	public void createTodo() {
		Date date = new Date();
		
		Post post = new Post();
		
		Todo todo = new Todo();
		
		TodoName todoName = new TodoName();
		
		TodoMember todoMember = new TodoMember();
	}
	
	// 투표(Vote) 생성
	public void createVote() {
		Date date = new Date();
		
		Post post = new Post();
		
		Vote vote = new Vote();
		
		VoteItem voteItem = new VoteItem();
		
		Voter voter = new Voter();
	}
}
