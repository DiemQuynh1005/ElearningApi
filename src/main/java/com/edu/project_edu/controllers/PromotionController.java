package com.edu.project_edu.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.project_edu.dto.PromotionDTO;
import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.services.PromotionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
  @Autowired
  PromotionService _promotionService;

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

}