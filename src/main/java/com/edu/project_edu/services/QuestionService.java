package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Question;
import com.edu.project_edu.repositories.QuestionRepository;

@Service
public class QuestionService {
  @Autowired
  QuestionRepository _questionRepository;

  public QuestionService(QuestionRepository questionRepository) {
    this._questionRepository = questionRepository;
  }

  public List<Question> getAllQuestions() {
    return _questionRepository.findAll();
  }

  public List<Question> getAllQuestionsByHomeworkId(int homeworkId) {
    return _questionRepository.findByHomeworkId(homeworkId);
  }

  public Question saveQuestion(Question question) {
    try {
      return _questionRepository.save(question);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void saveAllQuestions(List<Question> questions) {
    _questionRepository.saveAll(questions);
  }

  public Question detailQuestion(int id) {
    return _questionRepository.findById(id).orElse(null);
  }
}
