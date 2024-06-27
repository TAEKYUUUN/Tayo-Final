package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
