package com.mysite.tayo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ProjectMember;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{
	// 해당 멤버가 projectMember에 해당하는 프로젝트들 ( myProjectList)
	List<ProjectMember> findByMemberMemberIdx(Long memberIdx);
	
	// 해당 프로젝트의 projectMember 수를 카운트 => 참여중인 프로젝트 멤버 수
	@Query("SELECT count(*) FROM ProjectMember WHERE project.id = :projectIdx")
	Long countProjectMember(@Param("projectIdx") Long projectIdx);
	
	// 해당 프로젝트의 나에 대한 정보 -> projectMember 테이블에서의 나에대한 데이터 값
	Optional<ProjectMember> findByProjectProjectIdxAndMemberMemberIdx(Long projectIdx, Long memberIdx);
	
	// 해당 프로젝트의 모든 projectMember
	List<ProjectMember> findByProjectProjectIdx(Long projectIdx);
	
	// 해당 프로젝트의 나를 제외한 모든 projectMember
	List<ProjectMember> findByProjectProjectIdxAndMemberMemberIdxNot(Long projectIdx, Long memberIdx);
	
}
