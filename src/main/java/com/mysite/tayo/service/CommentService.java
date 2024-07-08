package com.mysite.tayo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.repository.CommentsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentsRepository commentsRepository;
	
	// 해당 포스트의 댓글들을 리스트로 Get
	public List<Comments> findByPost(Post post) {
		return commentsRepository.findByPost(post);
	}
	
	public void createComment(Member member, Post post, String content) {
		Date date = new Date();
		
		Comments comment = new Comments();
		comment.setMember(member);
		comment.setPost(post);
		comment.setContents(content);
		comment.setWriteTime(date);
		this.commentsRepository.save(comment);
	}
}
