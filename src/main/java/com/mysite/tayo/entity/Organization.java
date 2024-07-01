package com.mysite.tayo.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "organization")
public class Organization {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq")
	@SequenceGenerator(name = "organization_seq", sequenceName = "ORGANIZATION_SEQ", allocationSize = 1)
	@Column(name = "organization_idx")
	private Long organizationIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_idx")
	@JsonIgnore
	private Company company;
	
	@Column(name="og_manager_idx")
	private Integer ogManagerIdx;
	
	@Column(name="organization_name", length=100)
	private String organizationName;
	
	@Column(name="organization_order")
	private Integer organizationOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upper_organization")
	private Organization upperOrganization;
	
	@OneToMany(mappedBy = "upperOrganization", fetch = FetchType.LAZY)
	private List<Organization> lowerOrganization;
}
