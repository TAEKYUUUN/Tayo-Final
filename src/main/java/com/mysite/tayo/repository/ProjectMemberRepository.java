package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{

}
