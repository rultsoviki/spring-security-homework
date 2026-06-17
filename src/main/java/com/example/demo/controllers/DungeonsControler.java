package com.example.demo.controllers;

import com.example.demo.controllers.dto.DungeonRequest;
import com.example.demo.controllers.dto.DungeonResponse;
import com.example.demo.domain.Dungeon;
import com.example.demo.service.DungeonsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dungeons")
@AllArgsConstructor
public class DungeonsControler {
    private DungeonsService dungeonsService;

    @PostMapping("/{id}")
    public DungeonResponse getDungeon(@PathVariable Long id) {
        return dungeonsService.getDungeonWithMonsters(id);
    }

    @PostMapping("/{id}/attack")
    public String attack(@PathVariable Long id) {
        return dungeonsService.attackMonster(id);
    }
    @PostMapping
    public DungeonResponse create(@RequestBody DungeonRequest dungeonRequest){
        return dungeonsService.create(dungeonRequest);
    }

}
