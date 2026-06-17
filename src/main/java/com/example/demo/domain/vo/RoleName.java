package com.example.demo.domain.vo;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_PLAYER("ROLE_PLAYER"),
    ROLE_MASTER("ROLE_MASTER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String name;


    RoleName(String name) {
        this.name = name;
    }

}
