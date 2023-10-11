package com.nadjiel.tasker.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository repository;
  
  @PostMapping("/")
  public UserModel create(@RequestBody UserModel userModel) {
    var user = this.repository.save(userModel);

    return user;
  }

}
