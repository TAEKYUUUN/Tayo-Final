package com.mysite.tayo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.UncheckComments;
import com.mysite.tayo.repository.AlarmRepository;
import com.mysite.tayo.repository.CommentsRepository;
import com.mysite.tayo.repository.UncheckCommentsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentsRepository commentsRepository;
	
	private final AlarmRepository alarmRepository;
	
	private final UncheckCommentsRepository uncheckRepository;
	
	// 해당 포스트의 댓글들을 리스트로 Get
	public List<Comments> findByPost(Post post) {
		return commentsRepository.findByPost(post);
	}
	
	// 댓글 등록 - 댓글, 알림, 미확인 댓글
	public void createComment(Member member, Post post, String content) {
		Date date = new Date();
		
		Comments comment = new Comments();
		comment.setMember(member);
		comment.setPost(post);
		comment.setContents(content);
		comment.setWriteTime(date);
		this.commentsRepository.save(comment);
		
		Alarm alarm = new Alarm();
		alarm.setMember(post.getMember());
		alarm.setAlarmType(2);
		alarm.setComments(comment);
		alarm.setAlarmTime(date);
		this.alarmRepository.save(alarm);
		
		UncheckComments uncheckComments = new UncheckComments();
		uncheckComments.setComments(comment);
		uncheckComments.setMember(post.getMember());
		this.uncheckRepository.save(uncheckComments);
	}
}
