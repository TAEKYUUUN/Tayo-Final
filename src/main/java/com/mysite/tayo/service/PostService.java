package com.mysite.tayo.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Comments;
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
	private final ParagraphRepository paragraphRepository;
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

	// 댓글 받아서 해당 포스트 idx의 포스트 가져오기
	public Optional<Post> getPostByComment(Comments comment) {
		return postRepository.findById(comment.getPost().getPostIdx());
	}
	
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
		this.paragraphRepository.save(paragraph);

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
		task.setManager (manager);
		task.setStartDate(date);
		task.setEndDate(endDate);
		task.setContents(contents);
		this.taskRepository.save(task);
		
		// 하위업무의 존재 여부 확인 -> 아닐 경우에 하위업무 db에 추가하는 작업 진행
		if(lowerTaskNameList != null) {
			// 이후 하위업무 name, condition 세팅
			for(int i=0; i<lowerTaskNameList.size(); i++) {
				LowerTask lowerTask = new LowerTask();
				lowerTask.setTask(task);
				lowerTask.setTaskName(lowerTaskNameList.get(i));
				lowerTask.setCondition(lowertTaskConditionList.get(i));
				this.lowerTaskRepository.save(lowerTask);
			}
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
			String place, String placeId, Double placeLat, Double placeLng, String contents, List<Member> scheduleAttenderList) {
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
		schedule.setPost(post);
		schedule.setEndDate(endDate);
		schedule.setPlace(place);
		schedule.setPlaceId(placeId);
		schedule.setPlaceLat(placeLat);
		schedule.setPlaceLng(placeLng);
		schedule.setContents(contents);
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
	public void createTodo(Member member, Project project, String title, 
			Member manager, Date deadLine, List<String> todoNameList) {
		Date date = new Date();

		Post post = new Post();
		post.setProject(project);
		post.setMember(member);
		post.setUploadDate(date);
		post.setFileType(4);
		this.postRepository.save(post);

		Todo todo = new Todo();
		todo.setTitle(title);
		todo.setPost(post);
		todo.setTodoManager(manager);
		todo.setDeadline(deadLine);
		this.todoRepository.save(todo);

		List<ProjectMember> projectMemberAll = projectMemberRepository.findByProjectProjectIdx(project.getProjectIdx());

		for (int i = 0; i < todoNameList.size(); i++) {
			TodoName todoName = new TodoName();
			todoName.setTodo(todo);
			todoName.setTodoName(todoNameList.get(i));
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
			List<String> voteItemList, Integer isPlural, Integer isAnonymous) {
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
		vote.setIsPlural(isPlural);
		vote.setIsAnonymous(isAnonymous);
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
	
	// postId를 받아서 글(Paragraph)의 데이터 가져오기
	public Map<String, String> getParagraph(Long postIdx) {
		Optional<Post> postOptional = postRepository.findById(postIdx);
		if(postOptional.isPresent()) {
			Post post = postOptional.get();
			Paragraph paragraph = post.getParagraph();
			if(paragraph != null) {
				Map<String, String> paragraphData = new HashMap<>();
				paragraphData.put("title", paragraph.getTitle());
				paragraphData.put("contents", paragraph.getContents());
				return paragraphData;
			}
		}
		
		return Collections.emptyMap();
	}
	
	// postId를 받아서 업무(Task)의 데이터 가져오기
	public Map<String, Object> getTask(Long postIdx) {
	    Optional<Post> postOptional = postRepository.findById(postIdx);
	    if (postOptional.isPresent()) {
	        Post post = postOptional.get();
	        Task task = post.getTask();
	        if (task != null) {
	            Map<String, Object> taskData = new HashMap<>();
	            taskData.put("taskName", task.getTaskName());
	            taskData.put("condition", Integer.toString(task.getCondition()));
	            taskData.put("manager", task.getManager());	// manager 객체 추가
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            taskData.put("endDate", dateFormat.format(task.getEndDate()));

	            taskData.put("contents", task.getContents());
	            taskData.put("taskId", Long.toString(task.getTaskIdx()));

	            // 하위 업무가 존재하는지 확인하고, 비어있지 않다면 진행
	            List<LowerTask> lowerTasks = task.getLowerTasks();
	            if (lowerTasks != null && !lowerTasks.isEmpty()) {
	                List<Map<String, String>> lowerTasksData = new ArrayList<>();
	                for (LowerTask lowerTask : lowerTasks) {
	                    Map<String, String> lowerTaskData = new HashMap<>();
	                    lowerTaskData.put("LowerTaskIdx", Long.toString(lowerTask.getLowerTaskIdx()));
	                    lowerTaskData.put("taskName", lowerTask.getTaskName());
	                    lowerTaskData.put("condition", Integer.toString(lowerTask.getCondition()));
	                    lowerTasksData.add(lowerTaskData);
	                }
	                taskData.put("lowerTasks", lowerTasksData);
	            } else {
	                taskData.put("lowerTasks", Collections.emptyList());
	            }

	            return taskData;
	        }
	    }

	    return Collections.emptyMap();
	}
	
	// postIdx를 받아서 일정(Schedule)의 데이터 가져오기
	public Map<String, Object> getSchedule(Long postIdx) {
	    Optional<Post> postOptional = postRepository.findById(postIdx);
	    if (postOptional.isPresent()) {
	        Post post = postOptional.get();
	        Schedule schedule = post.getSchedule();
	        if (schedule != null) {
	            Map<String, Object> scheduleData = new HashMap<>();
	            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd (E), HH:mm");
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (E)");
	            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

	            Date startDate = schedule.getStartDate();
	            Date endDate = schedule.getEndDate();
	            
	            if (startDate == null || endDate == null) {
	                throw new IllegalArgumentException("Start date and end date must not be null");
	            }

	            String startDateStr = dateTimeFormat.format(startDate);
	            String endDateStr;
	            if (dateFormat.format(startDate).equals(dateFormat.format(endDate))) {
	                endDateStr = "~ " + timeFormat.format(endDate);
	            } else {
	                endDateStr = dateTimeFormat.format(endDate);
	            }

	            scheduleData.put("title", schedule.getTitle());
	            scheduleData.put("startDate", startDateStr);
	            scheduleData.put("endDate", endDateStr);
	            scheduleData.put("place", schedule.getPlace());
	            scheduleData.put("contents", schedule.getContents());
	            scheduleData.put("placeId", schedule.getPlaceId());
	            scheduleData.put("placeLat", schedule.getPlaceLat());
	            scheduleData.put("placeLng", schedule.getPlaceLng());

	            // startDate의 month와 day를 별도로 추출하여 추가
	            String startMonth = monthFormat.format(startDate);
	            LocalDate startDateLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            scheduleData.put("startMonth", startMonth);
	            scheduleData.put("startDay", startDateLocal.getDayOfMonth());
	            
	            // 스케줄 참석자 리스트를 추가
	            List<ScheduleAttender> attenders = schedule.getScheduleAttenders();
	            if (attenders != null && !attenders.isEmpty()) {
	                List<Map<String, String>> attendersData = new ArrayList<>();
	                for (ScheduleAttender attender : attenders) {
	                    Map<String, String> attenderData = new HashMap<>();
	                    attenderData.put("scheduleAttenderIdx", Long.toString(attender.getScheduleAttenderIdx()));
	                    attenderData.put("memberIdx", attender.getMember().getMemberIdx().toString());
	                    attenderData.put("memberName", attender.getMember().getName());
	                    attenderData.put("memberProfileImg", attender.getMember().getProfileImage());
	                    attenderData.put("isAttend", attender.getIsAttend() != null ? Integer.toString(attender.getIsAttend()) : "0");
	                    attendersData.add(attenderData);
	                }
	                scheduleData.put("attenders", attendersData);
	            } else {
	                scheduleData.put("attenders", Collections.emptyList());
	            }

	            return scheduleData;
	        }
	    }

	    return Collections.emptyMap();
	}

	// postIdx를 받아서 할 일(Todo)의 데이터 가져오기
	public Map<String, Object> getTodo(Long postIdx) {
	    Optional<Post> postOptional = postRepository.findById(postIdx);
	    if (postOptional.isPresent()) {
	    	Post post = postOptional.get();
	        Todo todo = post.getTodo();
	        if (todo != null) {
	            Map<String, Object> todoData = new HashMap<>();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (E)");
	            todoData.put("title", todo.getTitle());
	            todoData.put("todoManagerIdx", (todo.getTodoManager() != null) ? todo.getTodoManager().getMemberIdx().toString() : "null");
	            todoData.put("todoManagerName", (todo.getTodoManager() != null) ? todo.getTodoManager().getName() : "null");
	            todoData.put("todoManagerProfileImg", (todo.getTodoManager() != null) ? todo.getTodoManager().getProfileImage() : "null");
	            todoData.put("deadLine", todo.getDeadline() != null ? dateFormat.format(todo.getDeadline()) : "No deadLine");
	            // 할일 항목 리스트 추가
	            List<TodoName> todoNames = todo.getTodoNames();
	            if(todoNames != null && !todoNames.isEmpty()) {
	            	List<Map<String, String>> todoNamesData = new ArrayList<>();
	            	for(TodoName todoName : todoNames) {
	            		Map<String, String> todoNameData = new HashMap<>();
	            		todoNameData.put("todoName", todoName.getTodoName());
	            		todoNamesData.add(todoNameData);
	            	}
	            	todoData.put("todoNames", todoNamesData);
	            }
	            
	            return todoData;
	        }
	    }
	    return Collections.emptyMap();
	}
	// postIdx를 받아서 투표(Vote)의 데이터 가져오기
	public Map<String, Object> getVote(Long postIdx) {
		Optional<Post> postOptional = postRepository.findById(postIdx);
		if(postOptional.isPresent()) {
			Post post = postOptional.get();
			Vote vote = post.getVote();
			if(vote != null) {
				Map<String, Object> voteData = new HashMap<>();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (E)");
				voteData.put("title", vote.getTitle());
				voteData.put("voteDetail", vote.getVoteDetail());
				voteData.put("voteEndDate", vote.getVoteEndDate() != null ? dateFormat.format(vote.getVoteEndDate()) : "No end Date");
				voteData.put("endVote", vote.getEndVote() != null ? Integer.toString(vote.getEndVote()) : "0");
				voteData.put("isPlural", Integer.toString(vote.getIsPlural()));
				voteData.put("isAnonymous", Integer.toString(vote.getIsAnonymous()));

				// 투표 항목 리스트 추가
				List<VoteItem> voteItems = vote.getVoteItems();
				if(voteItems != null && !voteItems.isEmpty()) {
					List<Map<String, String>> voteItemsData = new ArrayList<>();
					for(VoteItem voteItem : voteItems) {
						Map<String, String> voteItemData = new HashMap<>();
						voteItemData.put("itemName", voteItem.getItemName());
						voteItemData.put("voteItemIdx", Long.toString(voteItem.getVoteItemIdx()));
						voteItemsData.add(voteItemData);
					}
					voteData.put("voteItems", voteItemsData);
				}
				
				return voteData;
			}
		}
		 return Collections.emptyMap();
	}
	
	// Post Delete : 프로젝트 관리자 or 글 작성자만 가능
	public void deletePostById(Long postIdx) {
		if(postRepository.existsById(postIdx)) {
			postRepository.deleteById(postIdx);
		} else {
			throw new IllegalArgumentException("Post with id" + postIdx + "does not exist.");
		}
	}
	
//	// Post Update : 글 작성자만 가능 (Paragraph, Task, Schedule, Todo, Vote)
//	public void updatePost(Long postIdx, Map<String, Object> params) {
//		Optional<Post> postOptional = postRepository.findById(postIdx);
//		Post post = postOptional.get();
//		int type = post.getFileType();
//		post.setIsRevised(1);
//		this.postRepository.save(post);
//		
//		switch(type) {
//		case 1: // Paragraph
//			updateParagraph(postIdx, params);
//			break;
//		case 2: // Task
//			updateTask(postIdx, params);
//			break;
//		case 3: // Schedule
//			updateSchedule(postIdx, params);
//			break;
//		case 4: // Todo
//			updateTodo(postIdx, params);
//			break;
//		case 5: // Vote
//			updateVote(postIdx, params);
//			break;
//			
//		}
//	}
//	
//	public void updateParagraph(Long postIdx, Map<String, Object> params) {
//		Optional<Paragraph> paragraphOptional = paragraphRepository.findByPostPostIdx(postIdx);
//	    Paragraph paragraph = paragraphOptional.get();
//	    paragraph.setTitle((String) params.get("title"));
//	    paragraph.setContents((String) params.get("contents"));
//	    this.paragraphRepository.save(paragraph);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void updateTask(Long postIdx, Map<String, Object> params) {
//		Optional<Task> taskOptional = taskRepository.findByPostPostIdx(postIdx);
//		Task task = taskOptional.get();
//	    task.setTaskName((String) params.get("taskName"));
//	    task.setCondition((Integer) params.get("condition"));
//	    task.setManager((Member) params.get("manager"));
//	    task.setEndDate((Date) params.get("endDate"));
//	    task.setContents((String) params.get("contents"));
//	    this.taskRepository.save(task);
//
//	    List<String> newLowerTaskNameList = (List<String>) params.get("lowerTaskNameList");
//	    List<Integer> newLowertTaskConditionList = (List<Integer>) params.get("lowerTaskConditionList");
//	    if (newLowerTaskNameList != null) {
//	        for (int i = 0; i < newLowerTaskNameList.size(); i++) {
//	            LowerTask lowerTask = new LowerTask();
//	            lowerTask.setTask(task);
//	            lowerTask.setTaskName(newLowerTaskNameList.get(i));
//	            lowerTask.setCondition(newLowertTaskConditionList.get(i));
//	            lowerTask.setEndDate(task.getEndDate());
//	            this.lowerTaskRepository.save(lowerTask);
//	        }
//	    }
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void updateSchedule(Long postIdx, Map<String, Object> params) {
//		Optional<Post> postOptional = postRepository.findById(postIdx);
//		Post post = postOptional.get();
//		post.setIsRevised(1);
//		this.postRepository.save(post);
//		
//		Optional<Schedule> scheduleOptional = scheduleRepository.findByPostPostIdx(postIdx);
//		Schedule schedule = scheduleOptional.get();
//		schedule.setTitle((String) params.get("title"));
//	    schedule.setStartDate((Date) params.get("startDate"));
//	    schedule.setEndDate((Date) params.get("endDate"));
//	    schedule.setPlace((String) params.get("place"));
//	    schedule.setContents((String) params.get("contents"));
//	    this.scheduleRepository.save(schedule);
//		
//	    List<Member> scheduleAttenderList = (List<Member>) params.get("scheduleAttenderList");
//	    if (scheduleAttenderList != null) {
//	        // update schedule attenders logic
//	    	// 참석자 추가 기능 이후에 고려 할 것.
//	    }
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void updateTodo(Long postIdx, Map<String, Object> params) {
//		Optional<Todo> todoOptional = todoRepository.findByPostPostIdx(postIdx);
//		Todo todo = todoOptional.get();
//		todo.setTitle((String) params.get("title"));
//	    this.todoRepository.save(todo);
//	    
//	    List<String> todoNameList = (List<String>) params.get("todoNameList");
//	    List<Member> todoManagerList = (List<Member>) params.get("todoManagerList");
//	    List<Date> todoDeadlineList = (List<Date>) params.get("todoDeadlineList");
//	    if (todoNameList != null && todoManagerList != null && todoDeadlineList != null) {
//	        // update todo items logic
//	    }
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void updateVote(Long postIdx, Map<String, Object> params) {
//		Optional<Vote> voteOptional = voteRepository.findByPostPostIdx(postIdx);
//	    Vote vote = voteOptional.get();
//	    vote.setTitle((String) params.get("title"));
//	    vote.setVoteDetail((String) params.get("voteDetail"));
//	    vote.setVoteEndDate((Date) params.get("voteEnddate"));
//	    vote.setIsPlural((Integer) params.get("isPlural"));
//	    vote.setIsAnonymous((Integer) params.get("isAnonymous"));
//	    this.voteRepository.save(vote);
//	    
//	    List<String> voteItemList = (List<String>) params.get("voteItemList");
//	    if (voteItemList != null) {
//	        // update vote items logic
//	    }
//	}
	
	
}
