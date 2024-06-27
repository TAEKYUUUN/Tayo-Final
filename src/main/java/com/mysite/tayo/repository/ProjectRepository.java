package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
