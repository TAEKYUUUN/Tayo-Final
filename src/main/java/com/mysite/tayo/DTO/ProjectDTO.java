package com.mysite.tayo.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysite.tayo.entity.ProjectMember;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {
	private Long projectIdx;
	private String projectName;
	private List<ProjectMember> projectMemberList;
	
	 @JsonProperty("isManager")
	private List<Integer> isManager;
	// 필요한 다른 필드 추가

	// getters and setters
}
