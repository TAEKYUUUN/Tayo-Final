package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.UncheckCommentsReact;

@Repository
public interface UncheckCommentsReactRepository extends JpaRepository<UncheckCommentsReact, Long>{

}
