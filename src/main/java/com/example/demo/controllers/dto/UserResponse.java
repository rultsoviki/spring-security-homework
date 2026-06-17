package com.example.demo.controllers.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private int dmg;
    private int experience;
    private Set<String> roles;

}
