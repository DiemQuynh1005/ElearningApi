package com.edu.project_edu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubmissionDTO {
  private int account_id;
  private int homework_id;
  private List<UserAnswerDTO> userAnswers;
}
