package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.PostReact;

@Repository
public interface PostReactRepository extends JpaRepository<PostReact, Long>{

}
