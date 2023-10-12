package com.nadjiel.tasker.task;

import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository repository;

  @PostMapping("/")
  public ResponseEntity create(
    @RequestBody TaskModel model,
    HttpServletRequest request
  ) {
    // Getting owner id from request attributes
    UUID owner = (UUID) request.getAttribute("user");

    // Setting the owner id in the model
    model.setOwner(owner);

    // Verifying dates
    LocalDateTime currentDate = LocalDateTime.now();

    if(model.getStartsAt().isBefore(currentDate)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("startsAt can't be a past date!");
    }

    if(model.getFinishesAt().isBefore(model.getStartsAt())) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("finishesAt can't be prior to the startsAt!");
    }

    // Saving the task on the database
    TaskModel task = repository.save(model);

    // Returning result
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(task);
  }

}
