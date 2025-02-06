package com.edu.project_edu.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course extends BaseEntity {
  @Column(name = "name")
  private String name;

  @Column(name = "price", columnDefinition = "decimal(10,2)")
  private double price;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "thumbnail")
  private String thumbnail;

  @Column(name = "duration")
  // phut
  private int duration;

  @Column(name = "status")
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "cousre", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<Subscription> subscriptions;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<Lesson> lessons;

}
