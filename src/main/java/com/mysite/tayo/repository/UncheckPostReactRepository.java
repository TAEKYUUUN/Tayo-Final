package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.UncheckPostReact;

@Repository
public interface UncheckPostReactRepository extends JpaRepository<UncheckPostReact, Long>{

}
