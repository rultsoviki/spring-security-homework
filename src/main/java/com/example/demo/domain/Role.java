package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}