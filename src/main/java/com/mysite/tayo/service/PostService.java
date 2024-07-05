package com.mysite.tayo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Alarm;
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
import com.mysite.tayo.entity.UncheckPost;
import com.mysite.tayo.entity.Unread;
import com.mysite.tayo.entity.Vote;
import com.mysite.tayo.entity.VoteItem;
import com.mysite.tayo.entity.Voter;
import com.mysite.tayo.repository.AlarmRepository;
import com.mysite.tayo.repository.LowerTaskRepository;
import com.mysite.tayo.repository.ParagraphRepository;
import com.mysite.tayo.repository.PostMemberRepository;
import com.mysite.tayo.repository.PostRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ScheduleAttenderRepository;
import com.mysite.tayo.repository.ScheduleRepository;
import com.mysite.tayo.repository.TaskRepository;
import com.mysite.tayo.repository.TodoMemberRepository;
import com.mysite.tayo.repository.TodoNameRepository;
import com.mysite.tayo.repository.TodoRepository;
import com.mysite.tayo.repository.UncheckPostRepository;
import com.mysite.tayo.repository.UnreadRepository;
import com.mysite.tayo.repository.VoteItemRepository;
import com.mysite.tayo.repository.VoteRepository;
import com.mysite.tayo.repository.VoterRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final ProjectMemberRepository projectMemberRepository;
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
	private final UncheckPostRepository uncheckPostRepository;
	private final AlarmRepository alarmRepository;
	private final UnreadRepository unreadRepository;

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

		for (ProjectMember projectMember : projectMemberAll) {
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
		}
		// uncheck_post, alarm, unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		List<ProjectMember> projectMemberExcludeMe = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdxNot(project.getProjectIdx(), member.getMemberIdx());
		for (ProjectMember projectMember : projectMemberExcludeMe) {
			UncheckPost uncheckPost = new UncheckPost();
			uncheckPost.setPost(post);
			uncheckPost.setMember(projectMember.getMember());
			this.uncheckPostRepository.save(uncheckPost);

			Alarm alarm = new Alarm();
			alarm.setPost(post);
			alarm.setProject(projectMember.getProject());
			alarm.setMember(projectMember.getMember());
			alarm.setAlarmType(1);
			alarm.setAlarmTime(date);
			this.alarmRepository.save(alarm);

			Unread unread = new Unread();
			unread.setPost(post);
			this.unreadRepository.save(unread);
		}

	}

	// 업무(Task) 생성
	public void createTask(Member member, Project project, String taskName, int condition, Member manager,
			Date endDate, String contents, List<String> lowerTaskNameList, List<Integer> lowertTaskConditionList) {
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
		task.setMember(member);	// manager
		task.setStartDate(date);
		task.setEndDate(endDate);
		task.setUploadDate(date);
		task.setContents(contents);
		this.taskRepository.save(task);
		
		for(int i=0; i<lowerTaskNameList.size(); i++) {
			LowerTask lowerTask = new LowerTask();
			lowerTask.setTask(task);
			lowerTask.setTaskName(lowerTaskNameList.get(i));
			lowerTask.setCondition(lowertTaskConditionList.get(i));
			lowerTask.setUploadDate(date);
			lowerTask.setEndDate(endDate);
			this.lowerTaskRepository.save(lowerTask);
		}

		// post_member 에 프로젝트 참여중인 모든 멤버 추가 (작성자 포함)
		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());

		for (ProjectMember projectMember : projectMemberAll) {
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
		}
		// uncheck_post, alarm, unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		List<ProjectMember> projectMemberExcludeMe = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdxNot(project.getProjectIdx(), member.getMemberIdx());
		for (ProjectMember projectMember : projectMemberExcludeMe) {
			UncheckPost uncheckPost = new UncheckPost();
			uncheckPost.setPost(post);
			uncheckPost.setMember(projectMember.getMember());
			this.uncheckPostRepository.save(uncheckPost);

			Alarm alarm = new Alarm();
			alarm.setPost(post);
			alarm.setProject(projectMember.getProject());
			alarm.setMember(projectMember.getMember());
			alarm.setAlarmType(1);
			alarm.setAlarmTime(date);
			this.alarmRepository.save(alarm);

			Unread unread = new Unread();
			unread.setPost(post);
			this.unreadRepository.save(unread);
		}
	}

	// 일정(Schedule) 생성
	public void createSchedule(Member member, Project project, String title, Date startDate, Date endDate,
			String place, String contents, List<Member> scheduleAttenderList) {
		Date date = new Date();

		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(3);
		this.postRepository.save(post);

		Schedule schedule = new Schedule();
		schedule.setTitle(title);
		schedule.setStartDate(startDate);
		schedule.setEndDate(endDate);
		schedule.setPlace(place);
		this.scheduleRepository.save(schedule);

		for (Member attender : scheduleAttenderList) {
			ScheduleAttender scheduleAttender = new ScheduleAttender();
			scheduleAttender.setMember(attender);
			scheduleAttender.setSchedule(schedule);
			this.scheduleAttenderRepository.save(scheduleAttender);
		}

		// post_member 에 프로젝트 참여중인 모든 멤버 추가 (작성자 포함)
		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());

		for (ProjectMember projectMember : projectMemberAll) {
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
		}
		// uncheck_post, alarm, unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		List<ProjectMember> projectMemberExcludeMe = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdxNot(project.getProjectIdx(), member.getMemberIdx());
		for (ProjectMember projectMember : projectMemberExcludeMe) {
			UncheckPost uncheckPost = new UncheckPost();
			uncheckPost.setPost(post);
			uncheckPost.setMember(projectMember.getMember());
			this.uncheckPostRepository.save(uncheckPost);

			Alarm alarm = new Alarm();
			alarm.setPost(post);
			alarm.setProject(projectMember.getProject());
			alarm.setMember(projectMember.getMember());
			alarm.setAlarmType(1);
			alarm.setAlarmTime(date);
			this.alarmRepository.save(alarm);

			Unread unread = new Unread();
			unread.setPost(post);
			this.unreadRepository.save(unread);
		}

	}

	// 할일(Todo) 생성
	public void createTodo(Member member, Project project, String title, List<String> todoNameList,
			List<Member> todoManagerList, List<Date> todoDeadlineList) {
		Date date = new Date();

		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(4);
		this.postRepository.save(post);

		Todo todo = new Todo();
		todo.setTitle(title);
		this.todoRepository.save(todo);

		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());

		for (int i = 0; i < todoNameList.size(); i++) {
			TodoName todoName = new TodoName();
			todoName.setTodo(todo);
			todoName.setTodoName(todoNameList.get(i));
			todoName.setTodoManager(todoManagerList.get(i));
			todoName.setDeadline(todoDeadlineList.get(i));
			this.todoNameRepository.save(todoName);

			for (ProjectMember projectMember : projectMemberAll) {
				TodoMember todoMember = new TodoMember();
				todoMember.setTodoname(todoName);
				todoMember.setMember(projectMember.getMember());
				this.todoMemberRepository.save(todoMember);
			}
		}

		// post_member 에 프로젝트 참여중인 모든 멤버 추가 (작성자 포함)

		for (ProjectMember projectMember : projectMemberAll) {
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
		}

		// uncheck_post, alarm, unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		List<ProjectMember> projectMemberExcludeMe = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdxNot(project.getProjectIdx(), member.getMemberIdx());
		for (ProjectMember projectMember : projectMemberExcludeMe) {
			UncheckPost uncheckPost = new UncheckPost();
			uncheckPost.setPost(post);
			uncheckPost.setMember(projectMember.getMember());
			this.uncheckPostRepository.save(uncheckPost);

			Alarm alarm = new Alarm();
			alarm.setPost(post);
			alarm.setProject(projectMember.getProject());
			alarm.setMember(projectMember.getMember());
			alarm.setAlarmType(1);
			alarm.setAlarmTime(date);
			this.alarmRepository.save(alarm);

			Unread unread = new Unread();
			unread.setPost(post);
			this.unreadRepository.save(unread);
		}
	}

	// 투표(Vote) 생성
	public void createVote(Member member, Project project, String title, String voteDetail, Date voteEnddate,
			List<String> voteItemList) {
		Date date = new Date();

		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(5);
		this.postRepository.save(post);

		Vote vote = new Vote();
		vote.setPost(post);
		vote.setTitle(title);
		vote.setVoteDetail(voteDetail);
		vote.setVoteEndDate(voteEnddate);
		this.voteRepository.save(vote);

		for (int i = 0; i < voteItemList.size(); i++) {
			VoteItem voteItem = new VoteItem();
			voteItem.setVote(vote);
			voteItem.setItemName(voteItemList.get(i));
			this.voteItemRepository.save(voteItem);
		}
		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());

		for (ProjectMember projectMember : projectMemberAll) {
			// voter 에 프로젝트 모든 멤버 추가.
			Voter voter = new Voter();
			voter.setVote(vote);
			voter.setMember(projectMember.getMember());
			this.voterRepository.save(voter);
			
			// post_member 에 프로젝트 모든 멤버 추가.
			PostMember postMember = new PostMember();
			postMember.setPost(post);
			postMember.setMember(projectMember.getMember());
			this.postMemberRepository.save(postMember);
		}

		// uncheck_post, alarm, unread 에 프로젝트 참여중인 모든 멤버 추가 (작성자 제외)
		List<ProjectMember> projectMemberExcludeMe = projectMemberRepository
				.findByProjectProjectIdxAndMemberMemberIdxNot(project.getProjectIdx(), member.getMemberIdx());
		for (ProjectMember projectMember : projectMemberExcludeMe) {
			UncheckPost uncheckPost = new UncheckPost();
			uncheckPost.setPost(post);
			uncheckPost.setMember(projectMember.getMember());
			this.uncheckPostRepository.save(uncheckPost);

			Alarm alarm = new Alarm();
			alarm.setPost(post);
			alarm.setProject(projectMember.getProject());
			alarm.setMember(projectMember.getMember());
			alarm.setAlarmType(1);
			alarm.setAlarmTime(date);
			this.alarmRepository.save(alarm);

			Unread unread = new Unread();
			unread.setPost(post);
			this.unreadRepository.save(unread);
		}
	}
}
