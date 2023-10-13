package com.nadjiel.tasker.task;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.nadjiel.tasker.utils.Utils;

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

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    // Getting owner id from request attributes
    UUID owner = (UUID) request.getAttribute("user");

    // Getting task list from the repository
    List<TaskModel> tasks = repository.findByOwner(owner);

    // Returning result
    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity update(
    @RequestBody TaskModel model,
    @PathVariable UUID id,
    HttpServletRequest request
  ) {
    // Getting owner id from request attributes
    UUID owner = (UUID) request.getAttribute("user");

    // Getting the task to be updated with the repository
    TaskModel task = repository.findById(id).orElse(null);

    // Verifying if task exists
    if(task == null) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Task not found!");
    }

    // Verifying if the requester is the task owner
    if(!task.getOwner().equals(owner)) {
      return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body("Unauthorized to update this task!");
    }

    // Setting only the received properties to the task model
    Utils.copyNonNullProperties(model, task);

    // Saving the updated model
    task = repository.save(task);

    // Returning the result
    return ResponseEntity
      .ok()
      .body(task);
  }

}
