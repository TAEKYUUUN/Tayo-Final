package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.UncheckComments;

@Repository
public interface UncheckCommentsRepository extends JpaRepository<UncheckComments, Long>{

}
