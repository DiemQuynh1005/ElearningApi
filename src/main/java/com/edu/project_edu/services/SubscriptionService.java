package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Subscription;
import com.edu.project_edu.repositories.SubscriptionRepository;

@Service
public class SubscriptionService {
  @Autowired
  SubscriptionRepository _subscriptionRepository;

  public List<Subscription> getAllSubscriptions() {
    return _subscriptionRepository.findAll();
  }

  public void saveAllSubscriptions(List<Subscription> subscriptions) {
    _subscriptionRepository.saveAll(subscriptions);
  }
}
