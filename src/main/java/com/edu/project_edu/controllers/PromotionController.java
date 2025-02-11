package com.edu.project_edu.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.dto.CheckPromotionRequest;
import com.edu.project_edu.dto.PromotionDTO;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.services.AccountService;
import com.edu.project_edu.services.PromotionService;
import com.edu.project_edu.services.PromotionUsageService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
  @Autowired
  PromotionService _promotionService;

  @Autowired
  AccountService _accountService;

  @Autowired
  PromotionUsageService _promotionUsageService;

  /*
   * ----------------- GET ALL PROMOTIONS -----------------
   */
  @GetMapping()
  public ResponseEntity<List<Promotion>> getAllPromotions() {
    return ResponseEntity.ok(_promotionService.getAllPromotions());
  }

  /*
   * ----------------- CREATE NEW PROMOTION -----------------
   */
  @PostMapping("/create")
  public ResponseEntity<Promotion> create(@Valid @RequestBody PromotionDTO promotionDTO,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }
    try {
      Promotion promotion = new Promotion();
      BeanUtils.copyProperties(promotionDTO, promotion);
      Promotion rs = _promotionService.savePromotion(promotion);
      if (rs != null) {
        return new ResponseEntity<>(rs, HttpStatus.CREATED); // Return status 201
      }
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * -----------------CHECK PROMOTION BEFORE PAYMENT-----------------
   */
  @PostMapping(path = "/validUsage")
  public ResponseEntity<Promotion> checkPromotionBeforePayment(@Valid @RequestBody CheckPromotionRequest request,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }
    try {
      Account account = _accountService.getAccountById(request.getAccount_id());
      if (account != null) {
        Promotion promotion = _promotionService.getPromotionByCode(request.getPromotion_code());
        if (promotion != null) {
          if (promotion.isStatus() && _promotionService.isUsablePromotion(promotion)
              && request.getAmount() >= promotion.getMin_amount()) {
            // Check whether the user already used this promotion yet
            if (!_promotionUsageService.isPromotionUsedByUser(account, promotion)) {
              return ResponseEntity.ok(promotion);
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); // Return status 406
          }
          return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED); // Return status 405
        }
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return status 404
    } catch (

    Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }
}