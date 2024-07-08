package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Comments;
import com.mysite.tayo.entity.Post;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long>{
	List<Comments> findByPost(Post post);
}
