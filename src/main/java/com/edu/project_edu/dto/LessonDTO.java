package com.edu.project_edu.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {
  @NotEmpty(message = "Name is required")
  private String name;

  private boolean status;

  private MultipartFile video;

  // @PositiveOrZero
  // private int view;

  @Positive
  private int course_id;
}
