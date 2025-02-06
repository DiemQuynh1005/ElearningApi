package com.edu.project_edu.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.UserAnswer;
import com.edu.project_edu.repositories.UserAnswerRepository;

@Service
public class UserAnswerService {
  @Autowired
  UserAnswerRepository _answerRepository;

  public UserAnswer saveAnswer(UserAnswer answer) {
    try {
      return _answerRepository.save(answer);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void saveAllAnswers(List<UserAnswer> answers) {
    _answerRepository.saveAll(answers);
  }

  public int countCorrectAnswers(int userSubmissionId) {
    return _answerRepository.findByUserSubmissionId(userSubmissionId)
        .stream()
        .filter(UserAnswer::isResult)
        .collect(Collectors.toList())
        .size();
  }
}
