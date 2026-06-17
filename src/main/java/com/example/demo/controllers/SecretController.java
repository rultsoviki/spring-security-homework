package com.example.demo.controllers;

import org.mapstruct.Mapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secret")
public class SecretController {

    @GetMapping()
    public String toAuthUsers(){
        System.out.println("eeeeeeeeeee");
        return "ураааа";
    }
}
