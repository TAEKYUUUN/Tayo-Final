package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.ProjectFolder;

@Repository
public interface ProjectFolderRepository extends JpaRepository<ProjectFolder, Long>{

}
