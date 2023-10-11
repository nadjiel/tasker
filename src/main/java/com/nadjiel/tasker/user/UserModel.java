package com.nadjiel.tasker.user;

import java.util.UUID;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "t_users")
@Data
public class UserModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  
  @Column(unique = true)
  private String username;

  private String password;
  
  private String name;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
