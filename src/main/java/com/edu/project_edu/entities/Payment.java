package com.edu.project_edu.entities;

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
@Table(name = "tb_payment")
public class Payment extends BaseEntity {
  @Column(name = "amount", columnDefinition = "decimal(10,2)")
  private double amount;

  @ManyToOne
  @JoinColumn(name = "promotion_id", referencedColumnName = "id", nullable = true)
  private Promotion promotion;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private List<Subscription> subscriptions;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private List<PromotionUsage> promotionUsages;

  public Payment(double amount, Promotion promotion) {
    this.amount = amount;
    this.promotion = promotion;
  }

}
