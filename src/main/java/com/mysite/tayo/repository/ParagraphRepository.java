package com.mysite.tayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.tayo.entity.Paragraph;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

}
