package com.mysite.tayo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
	Optional<Member> findByName(String name);
	List<Member> findByCompanyCompanyIdx(Long companyIdx);
	
	//특정 회사의 멤버 중에서 , 주어진 멤버 id 리스트에 포함되지 않은 멤버들
	@Query("SELECT m FROM Member m WHERE m.company.companyIdx = :companyIdx AND m.memberIdx NOT IN (SELECT pm.member.memberIdx FROM ProjectMember pm WHERE pm.project.projectIdx = :projectIdx)")
    List<Member> findMembersNotInProject(@Param("companyIdx") Long companyIdx, @Param("projectIdx") Long projectIdx);
}
