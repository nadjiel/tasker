package com.nadjiel.tasker.task;

import java.util.UUID;
import java.time.LocalDateTime;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "t_tasks")
public class TaskModel {
  
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(length = 50)
  private String title;

  private String description;

  private String priority;

  private LocalDateTime startsAt;

  private LocalDateTime finishesAt;

  @CreationTimestamp
  private LocalDateTime createdAt;

  private UUID owner;

  public void setTitle(String title) throws Exception {
    if(title.length() > 50) {
      throw new Exception(
        "Title must have max of 50 characters!"
      );
    }

    this.title = title;
  }

}
