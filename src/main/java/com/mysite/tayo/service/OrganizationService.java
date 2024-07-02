package com.mysite.tayo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.tayo.repository.OrganizationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrganizationService {
	 @Autowired
	    private OrganizationRepository organizationRepository;

	    @Transactional
	    public void deleteOrganizationsByCompanyIdx(Long companyIdx) {
	        organizationRepository.deleteAllByCompanyCompanyIdx(companyIdx);
	    }
}
