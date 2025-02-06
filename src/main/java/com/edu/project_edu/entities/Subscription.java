package com.edu.project_edu.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_subscription")
public class Subscription extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @ManyToOne
  @JoinColumn(name = "course_id", referencedColumnName = "id")
  private Course course;

  @ManyToOne
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  private Payment payment;
}
