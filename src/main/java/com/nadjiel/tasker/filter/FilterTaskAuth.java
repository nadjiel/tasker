package com.nadjiel.tasker.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

import com.nadjiel.tasker.user.IUserRepository;
import com.nadjiel.tasker.user.UserModel;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  // TODO: handle no Athentication header requests
  // TODO: handle no password requests
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String servletPath = request.getServletPath();
    // Verifying route
    if(!servletPath.equals("/tasks/")) {
      filterChain.doFilter(request, response);

      return;
    }

    // Getting username and password from Authorization header
    String authHeader = request.getHeader("Authorization");
    String authEncoded = authHeader.substring("Basic".length()).trim();
    byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
    String authString = new String(authDecoded);

    String[] credentials = authString.split(":");
    String username = credentials[0];
    String password = credentials[1];

    // Verifying if user exists
    UserModel user = userRepository.findByUsername(username);

    if(user == null) {
      response.sendError(401);

      return;
    }

    // Verifying if password is right
    Result result = BCrypt.verifyer().verify(
      password.toCharArray(),
      user.getPassword()
    );

    if(!result.verified) {
      response.sendError(401);

      return;
    }

    // Setting the user id on the request attributes
    request.setAttribute("user", user.getId());

    // Passing the flow
    filterChain.doFilter(request, response);
  }

}
