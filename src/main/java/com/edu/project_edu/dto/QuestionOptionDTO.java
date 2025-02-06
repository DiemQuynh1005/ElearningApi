package com.edu.project_edu.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionDTO {
  @NotEmpty(message = "Text is required")
  private String optionText;

  private boolean result;

  // @Positive
  // private int question_id;
}
