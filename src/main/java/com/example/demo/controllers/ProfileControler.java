package com.example.demo.controllers;

import com.example.demo.controllers.dto.UserResponse;
import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserItemService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class ProfileControler {
    private UserService userService;
    private UserItemService userItemService;
    private UserMapper userMapper;

    @GetMapping("/me")
    public UserResponse getProfile(){
        return userMapper.toResponse(userService.getMyProfile());
    }

    @GetMapping("/inventory")
    public String inventory(){
        var user = userService.getMyProfile();
        return userItemService.inventory(user);
    }

}
