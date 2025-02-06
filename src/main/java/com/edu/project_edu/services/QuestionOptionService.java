package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.QuestionOption;
import com.edu.project_edu.repositories.QuestionOptionRepository;

@Service
public class QuestionOptionService {
  @Autowired
  QuestionOptionRepository _optionRepository;

  public List<QuestionOption> getAllOptions() {
    return _optionRepository.findAll();
  }

  public QuestionOption saveOption(QuestionOption option) {
    try {
      return _optionRepository.save(option);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void saveAllOptions(List<QuestionOption> options) {
    _optionRepository.saveAll(options);
  }

  public QuestionOption detailOption(int id) {
    return _optionRepository.findById(id).orElse(null);
  }

  public QuestionOption findCorrectOptionByQuestionId(Integer questionId) {
    List<QuestionOption> options = _optionRepository.findByQuestionId(questionId);
    return options.stream()
        .filter(QuestionOption::isCorrect)
        .findFirst()
        .orElse(null);
  }
}
