package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
