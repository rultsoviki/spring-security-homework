package com.example.demo.controllers.dto;

import com.example.demo.domain.Item;
import com.example.demo.domain.Monster;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MonstersResponse {
    private Long id;

    private String name;
    private Integer hp;
    private Integer attack;
    private Integer level;
    private List<Item> lootTable = new ArrayList<>();

}
