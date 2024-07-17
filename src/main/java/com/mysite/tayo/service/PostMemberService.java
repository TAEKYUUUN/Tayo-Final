package com.mysite.tayo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.entity.ScheduleAttender;
import com.mysite.tayo.entity.Task;
import com.mysite.tayo.entity.Todo;
import com.mysite.tayo.entity.TodoMember;
import com.mysite.tayo.entity.TodoName;
import com.mysite.tayo.entity.Vote;
import com.mysite.tayo.entity.VoteItem;
import com.mysite.tayo.entity.Voter;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.ScheduleAttenderRepository;
import com.mysite.tayo.repository.TaskRepository;
import com.mysite.tayo.repository.TodoMemberRepository;
import com.mysite.tayo.repository.TodoNameRepository;
import com.mysite.tayo.repository.TodoRepository;
import com.mysite.tayo.repository.VoteItemRepository;
import com.mysite.tayo.repository.VoteRepository;
import com.mysite.tayo.repository.VoterRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostMemberService {
	
	private final TaskRepository taskRepository;
	private final ScheduleAttenderRepository scheduleAttenderRepository;
	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;
	private final TodoNameRepository todoNameRepository;
	private final TodoMemberRepository todoMemberRepository;
	private final VoteRepository voteRepository;
	private final VoterRepository voterRepository;
	private final VoteItemRepository voteItemRepository;
	
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
	
	
	// Vote - 투표 기능
	public void participateVote(Long postIdx, Long memberIdx, List<Long> voteItemIdxs) throws Exception {
		Vote vote = voteRepository.findByPostPostIdx(postIdx).orElseThrow(() -> new Exception("Vote not found"));
        Member member = memberRepository.findById(memberIdx).orElseThrow(() -> new Exception("Member not found"));
	
		// 복수투표 불가 설정이면 한 개의 항목에만 투표할 수 있게 처리
        if (vote.getIsPlural() == 0 && voteItemIdxs.size() > 1) {
            throw new Exception("Multiple voting is not allowed");
        }

        // 이미 투표한 사용자는 투표할 수 없도록 처리
        List<Voter> existingVotes = voterRepository.findByVoteAndMember(vote, member);
        if (!existingVotes.isEmpty()) {
            throw new Exception("You have already voted");
        }

        for (Long voteItemIdx : voteItemIdxs) {
            VoteItem voteItem = voteItemRepository.findById(voteItemIdx).orElseThrow(() -> new Exception("Vote item not found"));

            Voter voter = new Voter();
            voter.setVote(vote);
            voter.setMember(member);
            voter.setVoteItem(voteItem);
            voterRepository.save(voter);
        }
	}
	
	// 투표종료 메서드
	public void endVote(Long postIdx) {
        // 게시물 ID로 투표를 찾아서 종료 상태로 변경
        Vote vote = voteRepository.findByPostPostIdx(postIdx)
            .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        // 종료 상태로 변경 (예를 들어, endVote 필드를 1로 설정)
        vote.setEndVote(1);
        voteRepository.save(vote);
    }
	
	// 투표결과 가져오는 메서드
	public Map<String, Integer> getVoteResults(Long postIdx) throws Exception {
        Vote vote = voteRepository.findByPostPostIdx(postIdx).orElseThrow(() -> new Exception("Vote not found"));

        Map<String, Integer> results = new HashMap<>();
        for (VoteItem item : vote.getVoteItems()) {
            results.put(item.getItemName(), item.getVoters().size());
        }
        return results;
    }
	
	
	// 포스트 반응 설정
}
