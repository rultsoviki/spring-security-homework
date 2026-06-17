package com.example.demo.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "bestiary")
public class Monster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer hp;
    private Integer attack;
    private Integer level;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "item_monster",
            joinColumns = @JoinColumn(name = "bestiary_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id")
    )
    private List<Item> lootTable = new ArrayList<>();
}
