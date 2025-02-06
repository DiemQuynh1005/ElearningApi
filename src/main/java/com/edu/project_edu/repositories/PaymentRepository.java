package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Payment;

@Component
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
