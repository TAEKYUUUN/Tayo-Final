package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Unread;

@Repository
public interface UnreadRepository extends JpaRepository<Unread, Long>{

}
