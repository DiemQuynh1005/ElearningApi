package com.edu.project_edu.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerDTO {
  @NotEmpty(message = "Answer is required")
  private String answer;
  private int question_id;
}
