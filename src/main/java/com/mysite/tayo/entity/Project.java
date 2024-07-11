package com.mysite.tayo.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name = "project_seq", sequenceName = "PROJECT_SEQ", allocationSize = 1)
    @Column(name = "project_idx")
	private Long projectIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", referencedColumnName = "company_idx")
	private Company company;
	
	@Column(name = "project_name", length = 100)
	private String projectName;
	
	@Column(name = "project_type")
	private Integer projectType;
	
	@Column(name = "category_idx")
	private Integer categoryIdx;
	
	@Column(name = "main_tab")
	private Integer mainTab;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "folder_idx")
	private Integer folderIdx;
	
	@Column(name = "without_confirm")
	private Integer withoutConfirm;
	
	@Column(name = "creator_idx")
	private Long creatorIdx;
	
	@Column(name = "used_template")
	private Integer usedTemplate;
	
	@Column(name = "color")
	private String color;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> postList;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<ProjectMember> projectMemberList;

}
