package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{

}
