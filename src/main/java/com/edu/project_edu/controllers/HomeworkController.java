package com.edu.project_edu.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.dto.HomeworkDTO;
import com.edu.project_edu.entities.Homework;
import com.edu.project_edu.entities.Lesson;
import com.edu.project_edu.entities.Question;
import com.edu.project_edu.entities.QuestionOption;
import com.edu.project_edu.services.HomeworkService;
import com.edu.project_edu.services.LessonService;
import com.edu.project_edu.services.QuestionOptionService;
import com.edu.project_edu.services.QuestionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {
  @Autowired
  HomeworkService _homeworkService;

  @Autowired
  QuestionService _questionService;

  @Autowired
  QuestionOptionService _optionService;

  @Autowired
  LessonService _lessonService;

  /*
   * ----------------- GET ALL HOMEWORKS -----------------
   */
  @GetMapping()
  public ResponseEntity<List<Homework>> getAllHomeworks() {
    return ResponseEntity.ok(_homeworkService.getAllHomeworks());
  }

  /*
   * ----------------- GET ALL HOMEWORKS BY LESSON ID-----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/by-lesson/{id}")
  public List<Homework> getAllHomeworksByLessonId(@PathVariable int id) {
    List<Homework> list = _homeworkService.getAllHomeworksByLessonId(id);
    return list;
  }

  /*
   * CREATE NEW HOMEWORK + QUESTION + QUESTION OPTIONS
   */
  @PostMapping("/create")
  public ResponseEntity<Homework> create(@Valid @RequestBody HomeworkDTO homeworkDto, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    try {
      Lesson lesson = _lessonService.detailLesson(homeworkDto.getLesson_id());
      if (lesson == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return status 404
      }
      Homework homework = new Homework();
      homework.setDuration(homeworkDto.getDuration());
      homework.setStatus(homeworkDto.isStatus());
      homework.setLesson(lesson);
      // Save the homework
      Homework savedHomework = _homeworkService.saveHomework(homework);
      // Handle successful save or potential exceptions
      if (savedHomework != null) {
        // Create and associate Questions and QuestionOptions
        homeworkDto.getQuestions().stream()
            .map(questionDto -> {
              Question question = new Question();
              question.setType(questionDto.getType());
              question.setQuestion(questionDto.getQuestion());
              question.setHomework(homework); // Set relationship
              _questionService.saveQuestion(question); // Save question
              // QuestionOption
              if (questionDto.getType().equals("OPTION")) {
                List<QuestionOption> options = questionDto.getOptions().stream()
                    .map(optionDto -> {
                      QuestionOption option = new QuestionOption();
                      option.setOptionText(optionDto.getOptionText());
                      option.setCorrect(optionDto.isResult());
                      option.setQuestion(question);
                      return option;
                    })
                    .collect(Collectors.toList());
                _optionService.saveAllOptions(options);
              }
              return question;
            })
            .collect(Collectors.toList());
        return new ResponseEntity<>(savedHomework, HttpStatus.CREATED); // Return status 201
      }
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

}
