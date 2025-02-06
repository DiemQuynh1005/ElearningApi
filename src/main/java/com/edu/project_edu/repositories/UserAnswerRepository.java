package com.edu.project_edu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.UserAnswer;

@Component
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
  List<UserAnswer> findByUserSubmissionId(int userSubmissionId);
}
