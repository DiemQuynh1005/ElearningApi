package com.edu.project_edu.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_promotion_usage")
public class PromotionUsage extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @ManyToOne
  @JoinColumn(name = "promotion_id", referencedColumnName = "id")
  private Promotion promotion;

  @ManyToOne
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  private Payment payment;
}
