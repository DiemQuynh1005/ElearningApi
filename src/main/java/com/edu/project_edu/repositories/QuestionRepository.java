package com.edu.project_edu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Question;

@Component
public interface QuestionRepository extends JpaRepository<Question, Integer> {
  List<Question> findByHomeworkId(int homeworkId);
}
