package com.edu.project_edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.entities.PromotionUsage;
import com.edu.project_edu.repositories.PromotionUsageRepository;

@Service
public class PromotionUsageService {
  @Autowired
  PromotionUsageRepository _usageRepository;

  public boolean isPromotionUsedByUser(Account account, Promotion promotion) {
    return _usageRepository.existsByAccountAndPromotion(account, promotion);
  }

  public PromotionUsage savePromotionUsage(PromotionUsage usage) {
    try {
      return _usageRepository.save(usage);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
