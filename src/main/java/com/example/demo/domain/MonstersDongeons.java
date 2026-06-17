package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "monsters_dongeons")
public class MonstersDongeons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dungeons_id")
    private Dungeon dungeon;

    @ManyToOne
    @JoinColumn(name = "bestiary_id")
    private Monster monsterTemplate;

    private Integer currentHp;

}
