package com.edu.project_edu.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_blog")
public class Blog extends BaseEntity {
  @Column(name = "title", columnDefinition = "text")
  private String title;

  @Column(name = "content", columnDefinition = "text")
  private String content;

  @Column(name = "image")
  private String image;
}
