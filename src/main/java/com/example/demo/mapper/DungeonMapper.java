package com.example.demo.mapper;

import com.example.demo.controllers.dto.DungeonResponse;
import com.example.demo.domain.Dungeon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = MonsterMapper.class)
public interface DungeonMapper {
    @Mapping(target = "monsters", source = "monsters")
    DungeonResponse to(Dungeon dungeon);
}
