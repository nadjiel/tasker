package com.nadjiel.tasker.task;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository repository;

  @PostMapping("/")
  public TaskModel create(
    @RequestBody TaskModel model,
    HttpServletRequest request
  ) {
    // Getting owner id from request attributes
    UUID owner = (UUID) request.getAttribute("user");

    // Setting the owner id in the model
    model.setOwner(owner);

    // Saving the task on the database
    TaskModel task = repository.save(model);

    // Returning result
    return task;
  }

}
