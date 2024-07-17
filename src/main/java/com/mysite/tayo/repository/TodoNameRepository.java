package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Todo;
import com.mysite.tayo.entity.TodoName;

import jakarta.transaction.Transactional;

@Repository
public interface TodoNameRepository extends JpaRepository<TodoName, Long>{

	@Transactional
    @Modifying
    @Query("DELETE FROM TodoName tn WHERE tn.todo = :todo")
    void deleteAllByTodo(@Param("todo") Todo todo);
}
