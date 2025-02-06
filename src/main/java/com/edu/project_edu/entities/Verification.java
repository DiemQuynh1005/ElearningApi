package com.edu.project_edu.entities;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tb_verifications")

@AllArgsConstructor
@NoArgsConstructor
public class Verification extends BaseEntity {
  @Column(name = "email")
  private String email;

  @Column(name = "token")
  private String token;

  @Column(name = "expired_at")
  private Timestamp expiredAt;
}
