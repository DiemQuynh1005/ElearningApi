package com.edu.project_edu.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
  @NotEmpty(message = "Code is required")
  // @Max(value = 10)
  private String code;

  private LocalDateTime expired_at;

  @Positive
  @Min(value = 5)
  @Max(value = 80)
  private double discount_rate;

  @Positive
  private int quantity;

  private boolean status;

  @Positive
  @Min(value = 10000)
  private double min_amount;
}
