package com.edu.project_edu.dto;

import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
  @Positive
  private int account_id;

  private String promotion_code;

  private List<Integer> list_course_id;
}
