package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Paragraph;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

}
