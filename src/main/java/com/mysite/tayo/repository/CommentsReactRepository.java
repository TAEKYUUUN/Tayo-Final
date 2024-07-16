package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.CommentsReact;

import jakarta.transaction.Transactional;

@Repository
public interface CommentsReactRepository extends JpaRepository<CommentsReact, Long>{
	
	@Query("SELECT COUNT(cr) FROM CommentsReact cr WHERE cr.comments.commentsIdx = :commentIdx")
    int countByCommentsIdx(@Param("commentIdx")Long commentIdx);
	
	@Query("SELECT COUNT(cr) > 0 FROM CommentsReact cr WHERE cr.comments.commentsIdx = :commentsIdx AND cr.member.memberIdx = :memberIdx")
    boolean existsByCommentsAndMember(@Param("commentsIdx") Long commentsIdx, @Param("memberIdx") Long memberIdx);
	
	void deleteByCommentsCommentsIdxAndMemberMemberIdx(Long commentsIdx, Long memberIdx);
}
