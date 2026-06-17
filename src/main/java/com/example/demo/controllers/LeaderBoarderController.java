package com.example.demo.controllers;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboarder")
@RequiredArgsConstructor
public class LeaderBoarderController {

    private final UserService userService;

    @GetMapping
    public List<User> allUsers() {
        return userService.topUsers();
    }
}
