package com.mysite.tayo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findByCompanyCompanyIdx(Long projectIdx);
}
