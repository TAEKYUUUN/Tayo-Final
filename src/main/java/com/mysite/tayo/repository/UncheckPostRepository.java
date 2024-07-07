package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.UncheckPost;

@Repository
public interface UncheckPostRepository extends JpaRepository<UncheckPost, Long>{

}
