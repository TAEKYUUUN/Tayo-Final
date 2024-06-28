package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.TodoName;

@Repository
public interface TodoNameRepository extends JpaRepository<TodoName, Long>{

}
