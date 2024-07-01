package com.mysite.tayo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.entity.Project;
import com.mysite.tayo.entity.ProjectMember;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.repository.ProjectMemberRepository;
import com.mysite.tayo.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final MemberRepository memberRepository;
	
	// 프로젝트 생성
	public void createProject(String projectName, Integer mainTab, Integer projectType, Integer withoutConfirm, Long memberIdx, Company company) {
		Date date = new Date();
		Optional<Member> optionalMember = memberRepository.findById(memberIdx);
		
		Project project = new Project();
		project.setProjectName(projectName);
		project.setMainTab(mainTab);
		project.setProjectType(projectType);
		project.setWithoutConfirm(withoutConfirm);
		project.setCreationDate(date);
		project.setCreatorIdx(memberIdx);
		project.setColor("#8b00EA");
		project.setCompany(company);
		this.projectRepository.save(project);
		
		ProjectMember projectMember = new ProjectMember();
		projectMember.setIsManager(1);
		projectMember.setProject(project);
		projectMember.setMember(optionalMember.get());
		projectMember.setCompany(optionalMember.get().getCompany());
		this.projectMemberRepository.save(projectMember);
	}
	
	// 내가 참여중인 프로젝트 리스트 Get
	public List<ProjectMember> getMyProject(Long memberIdx) {
		return this.projectMemberRepository.findByMemberMemberIdx(memberIdx);
	}
	
	// 내가 참여중인 프로젝트들의 참여자 수 Get
	public ArrayList<Long> getCountProjectMember(List<ProjectMember> projectList) {
		ArrayList<Long> countMember = new ArrayList<>();
		
		for(int i=0; i<projectList.size(); i++) {
			countMember.add(projectMemberRepository.countProjectMember(projectList.get(i).getProject().getProjectIdx()));
		}
		return countMember;
	}

	
}
