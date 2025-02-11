package com.edu.project_edu.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.dto.SubscriptionRequest;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Course;
import com.edu.project_edu.entities.Payment;
import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.entities.PromotionUsage;
import com.edu.project_edu.entities.Subscription;
import com.edu.project_edu.services.AccountService;
import com.edu.project_edu.services.CourseService;
import com.edu.project_edu.services.PaymentService;
import com.edu.project_edu.services.PromotionService;
import com.edu.project_edu.services.PromotionUsageService;
import com.edu.project_edu.services.SubscriptionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  @Autowired
  SubscriptionService _subscriptionService;

  @Autowired
  AccountService _accountService;

  @Autowired
  PaymentService _paymentService;

  @Autowired
  CourseService _courseService;

  @Autowired
  PromotionService _promotionService;

  @Autowired
  PromotionUsageService _promotionUsageService;

  /*
   * ----------------- GET ALL PROMOTIONS -----------------
   */
  @GetMapping()
  public ResponseEntity<List<Subscription>> getAllSubscriptions() {
    return ResponseEntity.ok(_subscriptionService.getAllSubscriptions());
  }

  /*
   * CREATE NEW SUBSCRIPTION + PAYMENT
   */
  @PostMapping("/create")
  public ResponseEntity<Subscription> create(@Valid @RequestBody SubscriptionRequest request, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    try {
      Account account = _accountService.getAccountById(request.getAccount_id());
      if (account != null) {
        // 1. Retrieve selected courses from the request
        List<Course> selectedCourses = _courseService.getCoursesByIds(request.getList_course_id());
        if (selectedCourses.size() == request.getList_course_id().size()) {
          // 2. Apply promotion if applicable
          Promotion promotion = null;
          if (request.getPromotion_code() != null && !request.getPromotion_code().isBlank()) {
            promotion = _promotionService.getPromotionByCode(request.getPromotion_code());
            // if (promotion != null && promotion.isStatus() &&
            // _promotionService.isUsablePromotion(promotion)
            // && totalFee >= promotion.getMin_amount()) {
            // Check whether the user already used this promotion yet
            if (promotion != null && promotion.isStatus()
                && !_promotionUsageService.isPromotionUsedByUser(account, promotion)) {
              _promotionService.minusOneQuantity(promotion);
            } else {
              return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); // Return status 406
            }
            // } else {
            // return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Return status 403
            // }
          }

          // 4. Create a payment
          Payment payment = new Payment();
          payment.setAmount(request.getTotalAmount());
          payment.setPromotion(promotion);
          Payment savePayment = _paymentService.savePayment(payment);
          // Payment savePayment = _paymentService.savePayment(new Payment(totalFee,
          // promotion));

          // 5. Create PromotionUsage if Promotion used
          if (savePayment.getPromotion() != null) {
            PromotionUsage promotionUsage = new PromotionUsage();
            promotionUsage.setAccount(account);
            promotionUsage.setPromotion(savePayment.getPromotion());
            promotionUsage.setPayment(savePayment);
            _promotionUsageService.savePromotionUsage(promotionUsage);
          }

          // 6. Create subscriptions for each selected course

          List<Subscription> subscriptions = selectedCourses.stream()
              .map(course -> {
                Subscription subscription = new Subscription();
                subscription.setAccount(account);
                subscription.setCourse(course);
                subscription.setPayment(savePayment);
                return subscription;
              })
              .collect(Collectors.toList());
          _subscriptionService.saveAllSubscriptions(subscriptions);

          return new ResponseEntity<>((subscriptions.get(0)), HttpStatus.CREATED); // Return status 201
        }
      }
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * ----------------- GET ALL SUBSCRIPTION BY ACCOUNT NAME-----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/by-account-name/{name}")
  public List<Subscription> getAllByAccountName(@PathVariable String name) {
    List<Subscription> list = _subscriptionService.getAllByAccountName(name);
    return list;
  }

}
