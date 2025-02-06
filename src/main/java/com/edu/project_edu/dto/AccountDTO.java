package com.edu.project_edu.dto;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
  @NotEmpty(message = "Name is required!")
  private String name;

  @NotEmpty(message = "Password is required!")
  private String password;

  // @NotEmpty(message = "Token is required!")
  // private String token;

  @NotEmpty(message = "Email is required!")
  @Email(message = "Invalid Email Format")
  private String email;

  // private boolean gender;

  @NotEmpty(message = "Phone is required!")
  private String phone;

  @NotEmpty(message = "Role is required!")
  private String role;

  private Date dob;

  private MultipartFile avatar;
}
