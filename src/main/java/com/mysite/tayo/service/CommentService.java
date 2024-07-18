package com.mysite.tayo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Alarm;
import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.CommentsReact;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.UncheckComments;
import com.mysite.tayo.repository.AlarmRepository;
import com.mysite.tayo.repository.CommentsReactRepository;
import com.mysite.tayo.repository.CommentsRepository;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.UncheckCommentsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentsRepository commentsRepository;
	
	private final AlarmRepository alarmRepository;
	
	private final UncheckCommentsRepository uncheckRepository;
	
	private final CommentsReactRepository commentsReactRepository;
	
	private final MemberRepository memberRepository;
	
	// 해당 포스트의 댓글들을 리스트로 Get
	public List<Comments> findByPost(Post post) {
		return commentsRepository.findByPost(post);
	}
	
	// 해당 댓글의 좋아요 수 카운트
	public int getLikeCount(Long commentsIdx) {
		return commentsReactRepository.countByCommentsIdx(commentsIdx);
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
	
	// 댓글 좋아요 설정
	public void checkCommentLike(Long commentIdx, Member member) {
		Date date = new Date();
		Optional<Comments> commentsOptional = commentsRepository.findById(commentIdx);
		if (commentsOptional.isPresent()) {
			Comments comment = commentsOptional.get();
			CommentsReact commentReact = new CommentsReact();
			commentReact.setComments(comment);
			commentReact.setMember(member);
			commentReact.setReact(1);
			commentReact.setReactTime(date);
			this.commentsReactRepository.save(commentReact);
			
			Alarm alarm = new Alarm();
			alarm.setAlarmTime(date);
			alarm.setAlarmType(4);
			alarm.setCommentsReact(commentReact);
			alarm.setMember(comment.getMember());
			this.alarmRepository.save(alarm);
		}
	}
	
	// 댓글 좋아요 취소
	@Transactional
	public void cancelCommentLike(Long commentIdx, Long memberIdx) {
	    try {
	        if (commentsReactRepository.existsByCommentsAndMember(commentIdx, memberIdx)) {
	            commentsReactRepository.deleteByCommentsCommentsIdxAndMemberMemberIdx(commentIdx, memberIdx);
	        }
	    } catch (DataIntegrityViolationException e) {
	    	e.getMessage();
	    }
	}
	
	// 댓글과 그 댓글의 좋아요 수를 한번에 가져오기
	public Map<Comments, Integer> getCommentsWithLikeCount(Post post, Member member) {
        List<Comments> comments = commentsRepository.findByPost(post);
        Map<Comments, Integer> commentsWithLikeCount = new HashMap<>();

        for (Comments comment : comments) {
            int likeCount = (int) commentsReactRepository.countByCommentsIdx(comment.getCommentsIdx());
            boolean isLiked = commentsReactRepository.existsByCommentsAndMember(comment.getCommentsIdx(), member.getMemberIdx()); // 현재 사용자가 이 댓글을 좋아요 했는지 여부 확인
            comment.setLiked(isLiked);
            commentsWithLikeCount.put(comment, likeCount);
        }

        return commentsWithLikeCount;
    }
	
	// 댓글 삭제
	@Transactional
    public boolean deleteComment(Long commentIdx) {
        try {
            commentsReactRepository.deleteByCommentsCommentsIdx(commentIdx);
            commentsRepository.deleteById(commentIdx);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
