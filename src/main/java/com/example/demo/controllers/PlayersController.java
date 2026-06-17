package com.example.demo.controllers;

import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
@AllArgsConstructor
public class PlayersController {
    private UserService userService;

    @DeleteMapping("/players/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String ban(@PathVariable Long id) {
        userService.banUser(id);
        return "Бан пользователю " + id;
    }
}
