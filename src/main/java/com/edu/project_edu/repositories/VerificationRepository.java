package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.project_edu.entities.Verification;

public interface VerificationRepository extends JpaRepository<Verification, Integer> {
  public Verification findByToken(String token);

  public Verification findByTokenAndEmail(String token, String email);
}
