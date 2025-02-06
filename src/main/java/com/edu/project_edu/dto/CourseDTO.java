package com.edu.project_edu.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
  @NotEmpty(message = "Name is required")
  private String name;

  @Positive
  @Min(value = 10000)
  private double price;

  @NotEmpty(message = "Description is required")
  private String description;

  private MultipartFile thumbnail;

  @Min(value = 20)
  // phut
  private int duration;

  private boolean status;

  private int account_id;
}
