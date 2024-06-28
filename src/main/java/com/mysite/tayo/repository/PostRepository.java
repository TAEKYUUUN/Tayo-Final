package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
