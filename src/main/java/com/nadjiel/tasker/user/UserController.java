package com.nadjiel.tasker.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository repository;
  
  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel model) {
    UserModel user = repository.findByUsername(model.getUsername());

    if(user != null) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("User already exists!");
    }

    String password = BCrypt
      .withDefaults()
      .hashToString(12, model.getPassword().toCharArray());

    model.setPassword(password);
    
    UserModel newUser = repository.save(model);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(newUser);
  }

}
