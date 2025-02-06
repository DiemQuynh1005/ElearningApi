package com.edu.project_edu.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tb_user_submission")
@AllArgsConstructor
@NoArgsConstructor
public class UserSubmission extends BaseEntity {
  @Column(name = "mark", columnDefinition = "decimal(10,2)")
  private double mark;

  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @ManyToOne
  @JoinColumn(name = "homework_id", referencedColumnName = "id")
  private Homework homework;

  @OneToMany(mappedBy = "userSubmission", cascade = CascadeType.PERSIST)
  @Transient
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserAnswer> userAnswers;
}
