package com.example.demo.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DungeonResponse {
    private Long id;
    private String name;
    private String difficulty;
    private List<MonstersResponse> monsters = new ArrayList<>();
}