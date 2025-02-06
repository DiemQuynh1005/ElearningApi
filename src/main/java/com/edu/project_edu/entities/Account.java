package com.edu.project_edu.entities;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {
  @Column(name = "name")
  private String name;

  @Temporal(TemporalType.DATE)
  @Column(name = "dob")
  private Date dob;

  @Column(unique = true, name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "phone")
  private String phone;

  @Column(name = "role")
  private String role; // USER-TEACHER-ADMIN

  @Column(name = "avatar")
  private String avatar;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<Subscription> subscriptions;

  @Transient
  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<UserSubmission> userSubmissions;
}
