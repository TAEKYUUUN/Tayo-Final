package com.mysite.tayo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.entity.ScheduleAttender;
import com.mysite.tayo.entity.Task;
import com.mysite.tayo.entity.Todo;
import com.mysite.tayo.entity.TodoMember;
import com.mysite.tayo.entity.TodoName;
import com.mysite.tayo.repository.ScheduleAttenderRepository;
import com.mysite.tayo.repository.TaskRepository;
import com.mysite.tayo.repository.TodoMemberRepository;
import com.mysite.tayo.repository.TodoNameRepository;
import com.mysite.tayo.repository.TodoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostMemberService {
	
	private final TaskRepository taskRepository;
	private final ScheduleAttenderRepository scheduleAttenderRepository;
	private final TodoRepository todoRepository;
	private final TodoNameRepository todoNameRepository;
	private final TodoMemberRepository todoMemberRepository;
	
	// Task - 진행상태 변경
	public void updateTaskCondition(Long postIdx, int newCondition, Long memberIdx) {
		Optional<Task> taskOptional = taskRepository.findByPostPostIdx(postIdx);
		if(taskOptional.isPresent()) {
			Task task = taskOptional.get();
			
			// 권한 확인 로직 추가
			boolean isAuthorized = isMemberAuthorized(task, memberIdx);
			if(isAuthorized) {
				task.setCondition(newCondition);
				this.taskRepository.save(task);
			}
		}
	}
	
	public boolean isMemberAuthorized(Task task, Long memberIdx) {
	    // 프로젝트 관리자 확인
	    List<ProjectMember> projectMemberList = task.getPost().getProject().getProjectMemberList();
	    boolean isProjectManager = projectMemberList.stream()
	            .filter(member -> member.getMember().getMemberIdx().equals(memberIdx))
	            .anyMatch(member -> member.getIsManager().equals(1));
	    
	    // 글 작성자 확인
	    boolean isPostAuthor = task.getPost().getMember().getMemberIdx().equals(memberIdx);
	    
	    // 업무 담당자 확인
	    boolean isTaskManager = task.getManager() != null && task.getManager().getMemberIdx().equals(memberIdx);
	    
	    return isProjectManager || isPostAuthor || isTaskManager;
	}
	
	// Schedule - 참석, 불참, 미정 (택 1) 등록하기
	public void checkScheduleAttendance(Long postIdx, Long memberIdx, int attendance) {

		Optional<ScheduleAttender> scheduleAttenderOptional = scheduleAttenderRepository
				.findBySchedulePostPostIdxAndMemberMemberIdx(postIdx, memberIdx);
		
		if(scheduleAttenderOptional.isPresent()) {
			ScheduleAttender scheduleAttender = scheduleAttenderOptional.get();
			
			scheduleAttender.setIsAttend(attendance);
			this.scheduleAttenderRepository.save(scheduleAttender);
		}

	}
	
	// Todo - 할 일 체크
	@Transactional
	public boolean updateTodoMemberStatus(Long todonameIdx, Long memberIdx, Integer isDone) {
	    Optional<TodoMember> todoMemberOptional = todoMemberRepository.findByTodoNameTodoNameIdxAndMemberMemberIdx(todonameIdx, memberIdx);
	    if (todoMemberOptional.isPresent()) {
	        TodoMember todoMember = todoMemberOptional.get();
	        todoMember.setIsDone(isDone);
	        todoMemberRepository.save(todoMember);

	        // Check if all TodoMember for the TodoName are done
	        TodoName todoName = todoMember.getTodoName();
	        boolean allMembersDone = todoName.getTodoMembers().stream()
	                                         .allMatch(member -> Objects.equals(member.getIsDone(), 1));
	        if (allMembersDone) {
	            todoName.setIsFinished(1);
	        } else {
	            todoName.setIsFinished(0);
	        }
	        todoNameRepository.save(todoName);

	        // Check if all TodoName for the Todo are finished
	        Todo todo = todoName.getTodo();
	        boolean allTodoNamesFinished = todo.getTodoNames().stream()
	                                           .allMatch(name -> Objects.equals(name.getIsFinished(), 1));
	        if (allTodoNamesFinished) {
	            todo.setIsEnded(1);
	        } else {
	            todo.setIsEnded(0);
	        }
	        todoRepository.save(todo);

	        return true;
	    }
	    return false;
	}
	
	
	// Vote - 복수투표 기능, 익명투표는 -> 투표자가 안보이는걸로..? 제일 마지막에 합시다
	
	// 포스트 반응 설정
}
