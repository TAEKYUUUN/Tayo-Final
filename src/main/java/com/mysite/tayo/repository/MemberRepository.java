package com.mysite.tayo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
	Optional<Member> findByName(String name);
	List<Member> findByCompanyCompanyIdx(Long companyIdx);
}
