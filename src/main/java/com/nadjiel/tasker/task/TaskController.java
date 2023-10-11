package com.nadjiel.tasker.task;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository repository;

  @PostMapping("/")
  public TaskModel create(@RequestBody TaskModel model) {
    TaskModel task = repository.save(model);

    return task;
  }

}
