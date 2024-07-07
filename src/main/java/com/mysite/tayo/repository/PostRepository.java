package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Post;
import com.mysite.tayo.entity.Project;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	// 해당 프로젝트에 해당하는 post들 list로 가져오기
	List<Post> findByProjectProjectIdx(Long projectIdx);
	
	// 게시물 uploadTime 기준 최신 순 정렬
	List<Post> findAllByProjectProjectIdxOrderByUploadDateDesc(Long projectIdx);
}
