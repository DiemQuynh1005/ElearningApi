package com.edu.project_edu.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDTO {
  @NotEmpty(message = "Title is required")
  private String title;

  @NotEmpty(message = "Content is required")
  private String content;

  private MultipartFile image;
}
