package com.edu.project_edu.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
  @NotEmpty(message = "Type is required")
  @Pattern(regexp = "^WRITING$|^OPTION$")
  private String type;

  @NotEmpty(message = "Question is required")
  private String question;

  // @Positive
  // private int homework_id;

  private List<QuestionOptionDTO> options;
}
