package com.edu.project_edu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.entities.Question;
import com.edu.project_edu.services.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
  @Autowired
  QuestionService _questionService;

  /*
   * ----------------- GET ALL QUESTIONS-----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public List<Question> getAllQuestions() {
    List<Question> list = _questionService.getAllQuestions();
    return list;
  }

  /*
   * ----------------- DETAIL QUESTION -----------------
   */
  @GetMapping(path = "/detail/{id}")
  public ResponseEntity<Question> detailQuestion(@PathVariable int id) {
    Question question = _questionService.detailQuestion(id);
    if (question != null) {
      return ResponseEntity.ok(question);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
