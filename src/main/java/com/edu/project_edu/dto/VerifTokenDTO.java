package com.edu.project_edu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifTokenDTO {
  @NotEmpty(message = "Email could not be empty")
  @Email(message = "Invalid Email")
  private String email;

  @NotEmpty(message = "Token is required!")
  private String token;
}
