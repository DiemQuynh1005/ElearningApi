package com.edu.project_edu.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
// đánh dấu nó là lớp cha của những lớp khác
@MappedSuperclass
@Data
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @CreatedDate()
  private Instant created_at;
}
