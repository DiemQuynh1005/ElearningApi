package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Promotion;

@Component
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
  Promotion findByCode(String code);
}
