package com.edu.project_edu.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Promotion;
import com.edu.project_edu.repositories.PromotionRepository;

@Service
public class PromotionService {
  @Autowired
  PromotionRepository _promotionRepository;

  public List<Promotion> getAllPromotions() {
    return _promotionRepository.findAll();
  }

  public Promotion detailPromotion(int id) {
    return _promotionRepository.findById(id).orElse(null);
  }

  public Promotion savePromotion(Promotion promotion) {
    try {
      return _promotionRepository.save(promotion);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Promotion getPromotionByCode(String code) {
    return _promotionRepository.findByCode(code);
  }

  /*
   * Có thể trả về int để hiển thị msg cho từng valid
   */
  public boolean isUsablePromotion(Promotion promotion) {
    ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    // Convert LocalDateTime to Instant in the specified zone
    Instant expirationInstant = promotion.getExpired_at().atZone(zoneId).toInstant();

    // Get current time in Instant
    Instant currentInstant = Instant.now();

    return !currentInstant.isAfter(expirationInstant) && promotion.getQuantity() > 0;
  }

  /*
   * Hàm cập nhật Quantity -1 khi Promotion đc sử dụng
   * và cập nhật Status nếu Quantity == 0
   */
  public void minusOneQuantity(Promotion promotion) {
    if (promotion.getQuantity() > 0) {
      promotion.setQuantity(promotion.getQuantity() - 1);
      if (promotion.getQuantity() == 0) {
        promotion.setStatus(false);
      }
      _promotionRepository.save(promotion);
    }
  }

  /*
   * Hàm cập nhật status cho tất cả Promotion đã quá hạn sử dụng + quantity == 0
   */
  public void updatePromotionStatus() {
    ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    List<Promotion> promotions = _promotionRepository.findAll();

    promotions.forEach(promotion -> {
      // Convert LocalDateTime to ZonedDateTime in the specified zone
      ZonedDateTime expirationDateTime = promotion.getExpired_at().atZone(zoneId);

      // Get current time in ZonedDateTime for comparison
      ZonedDateTime currentDateTime = ZonedDateTime.now(zoneId);

      // Check if expired or quantity is 0
      if (currentDateTime.isBefore(expirationDateTime) && promotion.getQuantity() > 0) {
        promotion.setStatus(true); // Promotion is still usable
      } else {
        promotion.setStatus(false); // Promotion is expired or out of stock
      }

      _promotionRepository.save(promotion); // Save the updated promotion
    });
  }
}
