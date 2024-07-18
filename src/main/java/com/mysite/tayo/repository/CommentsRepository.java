package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Post;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long>{
	
	// 댓글을 조회할 때 member 객체를 함께 가져오도록 @EntityGraph를 사용
	@EntityGraph(attributePaths = {"member"})
	List<Comments> findByPost(Post post);
}
