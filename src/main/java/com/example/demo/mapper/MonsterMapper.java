package com.example.demo.mapper;

import com.example.demo.controllers.dto.MonstersResponse;
import com.example.demo.domain.MonstersDongeons;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MonsterMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "monsterTemplate.name")
    @Mapping(target = "hp", source = "currentHp")
    @Mapping(target = "attack", source = "monsterTemplate.attack")
    @Mapping(target = "level", source = "monsterTemplate.level")
    @Mapping(target = "lootTable", source = "monsterTemplate.lootTable")
    MonstersResponse to(MonstersDongeons monstersDongeons);
}
