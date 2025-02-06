package com.edu.project_edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_question_option")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOption extends BaseEntity {
  @Column(nullable = false)
  private String optionText;

  @Column(nullable = false)
  private boolean isCorrect;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "question_id", referencedColumnName = "id")
  private Question question;

}
