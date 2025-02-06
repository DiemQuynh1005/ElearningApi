package com.edu.project_edu.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDTO {
  @Positive
  @Min(value = 5)
  private int duration;

  @Positive
  private int lesson_id;

  private boolean status;

  private List<QuestionDTO> questions;
}
