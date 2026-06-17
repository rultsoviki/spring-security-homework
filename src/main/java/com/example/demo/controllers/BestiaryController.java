package com.example.demo.controllers;

import com.example.demo.domain.Dungeon;
import com.example.demo.domain.Monster;
import com.example.demo.service.BestiaryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/bestiary")
@RequiredArgsConstructor
public class BestiaryController {
    private final BestiaryService bestiaryService;

    @GetMapping
    public List<Monster> allMonsters() {
        return bestiaryService.allMonsters();
    }

    @GetMapping("{id}")
    public Monster getMonster(@PathVariable Long id) {
        return bestiaryService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<?> createEntry(@RequestBody Monster monster) {
        var createdMonster = bestiaryService.create(monster);
        if (createdMonster) {
            return ResponseEntity
                    .created(URI.create("/api/bestiary/" + monster.getId()))
                    .body(createdMonster);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Монстр с таким именем уже существует");
        }
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public Monster update(@PathVariable Long id, @RequestBody Monster monster) {
        monster.setId(id);
        return bestiaryService.update(monster);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteEntry(@PathVariable Long id){
        bestiaryService.delete(id);
    }
}
