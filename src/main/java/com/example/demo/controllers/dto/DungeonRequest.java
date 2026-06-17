package com.example.demo.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DungeonRequest {
    private String name;
    private String difficulty;
    private List<MonstersResponse> monsters = new ArrayList<>();
}
