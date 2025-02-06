package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.entities.PromotionUsage;

@Component
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Integer> {
  boolean existsByAccountAndPromotion(Account account, Promotion promotion);
}
