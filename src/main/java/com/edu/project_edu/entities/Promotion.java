package com.edu.project_edu.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_promotion")
public class Promotion extends BaseEntity {
  @Column(name = "code", unique = true)
  private String code;

  @Column(name = "expired_at")
  private LocalDateTime expired_at;

  @Column(name = "discount_rate", columnDefinition = "decimal(10,2)")
  private double discount_rate;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "status")
  private boolean status;

  @Column(name = "min_amount", columnDefinition = "decimal(10,2)")
  private double min_amount;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "promotion")
  private List<Payment> payments;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "promotion")
  private List<PromotionUsage> promotionUsages;
}
