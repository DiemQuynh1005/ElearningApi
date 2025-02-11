package com.edu.project_edu.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPromotionRequest {
  @Positive
  private int account_id;

  @NotEmpty
  private String promotion_code;

  @Positive
  private double amount;
}
