package com.mysite.tayo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.tayo.DTO.OrganizationDTO;
import com.mysite.tayo.entity.Company;
import com.mysite.tayo.entity.Organization;
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
	    
	    public List<Organization> findByCompanyIdx(Long companyIdx){
	    	return organizationRepository.findByCompanyCompanyIdx(companyIdx);
	    }
	    
	    public Optional<Organization> findByOrganizationIdx(Long organizationIdx){
	    	return organizationRepository.findById(organizationIdx);
	    }
	    
	    public void createOrganization(OrganizationDTO organizationDTO, Company company, Optional<Organization> upperOrganization ) {
	    	Organization organization = new Organization();
			organization.setOrganizationName(organizationDTO.getOrganizationName());
			organization.setCompany(company);
			if(upperOrganization.isPresent()) {
				organization.setUpperOrganization(upperOrganization.get());
			}
			organizationRepository.save(organization);
	    }
	    
	    public void updateOrganization(OrganizationDTO organizationDTO) {
	    	Optional<Organization> _organization = organizationRepository.findById(organizationDTO.getOrganizationIdx());
			Organization organization = _organization.get();
			organization.setOrganizationName(organizationDTO.getOrganizationName());
			organizationRepository.save(organization);
	    }
	    
	    public void deleteOrganization(OrganizationDTO organizationDTO) {
	    	Optional<Organization> _organization = organizationRepository.findById(organizationDTO.getOrganizationIdx());
			Organization organization = _organization.get();
			organizationRepository.deleteById(organization.getOrganizationIdx());
	    }
	    
	   
}
