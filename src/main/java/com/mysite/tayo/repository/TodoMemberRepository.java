package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.TodoMember;

@Repository
public interface TodoMemberRepository extends JpaRepository<TodoMember, Long>{

}
