package com.edu.project_edu.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tb_homework")
@AllArgsConstructor
@NoArgsConstructor
public class Homework extends BaseEntity {
  private int duration;

  @Column(name = "status")
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "lesson_id", referencedColumnName = "id")
  private Lesson lesson;

  @OneToMany(mappedBy = "homework", cascade = CascadeType.PERSIST)
  // @Transient
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Question> questions;

  @OneToMany(mappedBy = "homework", cascade = CascadeType.PERSIST)
  @Transient
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserSubmission> userSubmissions;
}
