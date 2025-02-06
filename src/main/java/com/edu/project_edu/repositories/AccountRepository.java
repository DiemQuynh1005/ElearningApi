package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Account;

@Component
public interface AccountRepository extends JpaRepository<Account, Integer> {
  public Account findByEmail(String email);
}
