package com.edu.project_edu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.QuestionOption;

@Component
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {
  List<QuestionOption> findByQuestionId(Integer questionId);
}
