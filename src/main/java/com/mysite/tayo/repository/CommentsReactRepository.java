package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.CommentsReact;

@Repository
public interface CommentsReactRepository extends JpaRepository<CommentsReact, Long>{

}
