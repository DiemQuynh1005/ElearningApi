package com.edu.project_edu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Subscription;

@Component
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
