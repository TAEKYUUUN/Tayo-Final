package com.mysite.tayo.company;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	Optional<Company> findByUrl(String url);

	Optional<Company> findByManagerMemberIdx(Long managerIdx);
}
