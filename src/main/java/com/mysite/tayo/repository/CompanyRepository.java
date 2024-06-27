package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	Optional<Company> findByUrl(String url);

	Optional<Company> findByManagerMemberIdx(Long managerIdx);
}
