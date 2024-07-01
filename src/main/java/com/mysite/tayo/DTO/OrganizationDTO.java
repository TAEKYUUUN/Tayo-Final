package com.mysite.tayo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO {
	private Long organizationIdx;
	private String organizationName;
	private Long organizationOrder;
	private Long upperOrganization;
}
