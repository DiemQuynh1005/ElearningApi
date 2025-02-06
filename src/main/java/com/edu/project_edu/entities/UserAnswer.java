package com.edu.project_edu.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tb_user_answer")
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer extends BaseEntity {
  @Column(nullable = false)
  private String userAnswer;

  private boolean result;

  @ManyToOne
  @JoinColumn(name = "question_id", referencedColumnName = "id")
  private Question question;

  @ManyToOne
  @JoinColumn(name = "user_submission_id", referencedColumnName = "id")
  private UserSubmission userSubmission;

}
