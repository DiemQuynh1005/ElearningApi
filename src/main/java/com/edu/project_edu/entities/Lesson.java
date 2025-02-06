package com.edu.project_edu.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_lesson")
public class Lesson extends BaseEntity {
  @Column(name = "name")
  private String name;

  @Column(name = "status")
  private boolean status;

  @Column(name = "video")
  private String video;

  @Column(name = "view")
  private int view;

  @ManyToOne
  @JoinColumn(name = "course_id", referencedColumnName = "id")
  private Course course;

  @OneToMany(mappedBy = "lesson", cascade = CascadeType.PERSIST)
  @Transient
  // @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Homework> homeworks;
}
