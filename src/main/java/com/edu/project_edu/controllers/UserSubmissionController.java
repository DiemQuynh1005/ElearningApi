package com.edu.project_edu.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.dto.UserSubmissionDTO;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Homework;
import com.edu.project_edu.entities.Question;
import com.edu.project_edu.entities.QuestionOption;
import com.edu.project_edu.entities.UserAnswer;
import com.edu.project_edu.entities.UserSubmission;
import com.edu.project_edu.services.AccountService;
import com.edu.project_edu.services.HomeworkService;
import com.edu.project_edu.services.QuestionOptionService;
import com.edu.project_edu.services.QuestionService;
import com.edu.project_edu.services.UserAnswerService;
import com.edu.project_edu.services.UserSubmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/submissions")
public class UserSubmissionController {
  @Autowired
  UserSubmissionService _submissionService;

  @Autowired
  UserAnswerService _answerService;

  @Autowired
  AccountService _accountService;

  @Autowired
  HomeworkService _homeworkService;

  @Autowired
  QuestionService _questionService;

  @Autowired
  QuestionOptionService _optionService;

  /*
   * ----------------- GET ALL USER SUBMISSIONS -----------------
   */
  @GetMapping()
  public ResponseEntity<List<UserSubmission>> getAllSubmissions() {
    return ResponseEntity.ok(_submissionService.getAllSubmissions());
  }

  /*
   * CREATE NEW USER SUBMISSION + ANSWER
   */
  @PostMapping("/create")
  public ResponseEntity<UserSubmission> create(@Valid @RequestBody UserSubmissionDTO submissionDto, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    try {
      // Check Account
      Account account = _accountService.getAccountById(submissionDto.getAccount_id());
      if (account == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return status 401
      }
      // Check Homework
      Homework homework = _homeworkService.getHomeworkById(submissionDto.getHomework_id());
      if (homework == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return status 404
      }
      // Save Submission
      UserSubmission submission = new UserSubmission();
      submission.setMark(0);
      submission.setAccount(account);
      submission.setHomework(homework);
      UserSubmission savedSubmission = _submissionService.saveSubmission(submission);
      // Save Answer and Update mark
      List<UserAnswer> userAnswers = submissionDto.getUserAnswers().stream()
          .map(userAnswerDTO -> {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUserAnswer(userAnswerDTO.getAnswer());
            userAnswer.setUserSubmission(savedSubmission);
            // Find question by ID
            Question question = _questionService.detailQuestion(userAnswerDTO.getQuestion_id());
            userAnswer.setQuestion(question);
            if (question.getType().equals("OPTION")) {
              QuestionOption correctOption = _optionService.findCorrectOptionByQuestionId(question.getId());
              userAnswer
                  .setResult(correctOption != null && correctOption.getOptionText().equals(userAnswer.getUserAnswer()));
            }
            return userAnswer;
          })
          .collect(Collectors.toList());
      _answerService.saveAllAnswers(userAnswers);
      // Calculate total correct and total questions
      int totalCorrect = _answerService.countCorrectAnswers(savedSubmission.getId());
      int totalQuestions = _questionService.getAllQuestionsByHomeworkId(homework.getId()).size();
      double updatedMark = (double) totalCorrect / totalQuestions * 100;
      savedSubmission.setMark(updatedMark);

      // Update mark the submission
      UserSubmission updatedSubmission = _submissionService.saveSubmission(savedSubmission);
      return new ResponseEntity<>(updatedSubmission, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }
}
