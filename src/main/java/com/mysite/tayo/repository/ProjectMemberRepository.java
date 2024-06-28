package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ProjectMember;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{
	List<ProjectMember> findByMemberMemberIdx(Long memberIdx);
	
	@Query("SELECT count(*) FROM ProjectMember WHERE project.id = :projectIdx")
	Long countProjectMember(@Param("projectIdx") Long projectIdx);
}
