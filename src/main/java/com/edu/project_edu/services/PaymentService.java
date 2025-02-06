package com.edu.project_edu.services;

import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Payment;
import com.edu.project_edu.repositories.PaymentRepository;

@Service
public class PaymentService {
  private final PaymentRepository _paymentRepository;

  public PaymentService(PaymentRepository paymentRepository) {
    this._paymentRepository = paymentRepository;
  }

  public Payment savePayment(Payment payment) {
    try {
      return _paymentRepository.save(payment);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
