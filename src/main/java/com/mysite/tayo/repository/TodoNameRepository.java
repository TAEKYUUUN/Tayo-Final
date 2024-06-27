package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.TodoName;

public interface TodoNameRepository extends JpaRepository<TodoName, Long>{

}
