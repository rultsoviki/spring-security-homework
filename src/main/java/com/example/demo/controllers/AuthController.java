package com.example.demo.controllers;

import com.example.demo.controllers.dto.AuthResponse;
import com.example.demo.controllers.dto.RefreshRequest;
import com.example.demo.controllers.dto.UserRequest;
import com.example.demo.controllers.dto.UserResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    public UserService userService;
    public AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Validated @RequestBody UserRequest userRequest){
        return userService.save(userRequest);
    }

    @PostMapping("/login")
  public AuthResponse auth(@RequestBody UserRequest userRequest){
        return authService.login(userRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request){
        return authService.refresh(request.getRefreshToken());
    }
}
