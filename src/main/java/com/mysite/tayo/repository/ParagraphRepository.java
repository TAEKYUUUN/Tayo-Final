package com.mysite.tayo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.tayo.entity.Paragraph;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
	// postIdx 를 갖고 해당하는 Paragraph 가져오기
	Optional<Paragraph> findByPostPostIdx(Long postIdx);
}
