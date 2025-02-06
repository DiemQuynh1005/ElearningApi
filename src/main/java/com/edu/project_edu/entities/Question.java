package com.edu.project_edu.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {
  @Column(name = "type", nullable = false)
  private String type; // WRITING | OPTION

  @Column(columnDefinition = "text", nullable = false)
  private String question;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "homework_id", referencedColumnName = "id")
  private Homework homework;

  // @Transient
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
  private List<QuestionOption> options;

  @Transient
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
  private List<UserAnswer> userAnswers;
}
