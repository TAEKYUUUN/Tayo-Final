package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long>{
	Optional<Organization> findByOrganizationName(String organizationName);
}
