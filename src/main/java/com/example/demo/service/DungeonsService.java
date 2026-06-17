package com.example.demo.service;

import com.example.demo.controllers.dto.DungeonRequest;
import com.example.demo.controllers.dto.DungeonResponse;
import com.example.demo.domain.*;
import com.example.demo.mapper.DungeonMapper;
import com.example.demo.mapper.MonsterMapper;
import com.example.demo.repository.DungeonRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DungeonsService {
    private DungeonRepository dungeonRepository;
    private UserService userService;
    private MonstersDungeonService monstersDungeonService;
    private UserItemService userItemService;
    private final DungeonMapper dungeonMapper;

    public DungeonResponse create(DungeonRequest dungeonRequest) {
        if (dungeonRepository.existsByName(dungeonRequest.getName()))
            throw new RuntimeException("Такое подземелье уже есть");
        Dungeon dungeon = new Dungeon(null, dungeonRequest.getName(), dungeonRequest.getDifficulty(), null);

        return dungeonMapper.to(dungeonRepository.save(dungeon));
    }

    public DungeonResponse getDungeonWithMonsters(Long id) {
        Dungeon dungeon = dungeonRepository.findByIdWithMonsters(id)
                .orElseThrow(() -> new RuntimeException("Dungeon not found"));
        return dungeonMapper.to(dungeon);
    }

    @Transactional
    public String attackMonster(Long monsterId) {
        User user = userService.getMyProfile(); // может бросить исключение, если не авторизован
        var monster = monstersDungeonService.findById(monsterId);

        int newHp = monster.getCurrentHp() - user.getDmg();
        if (newHp <= 0) {
            // Монстр убит
            monster.setCurrentHp(0);
            monstersDungeonService.update(monster); // сохраняем изменения
            String loot = upDrop(user, monster.getMonsterTemplate().getLootTable());
            monstersDungeonService.delete(monster);
            return String.format("%s атаковал %s и убил его! Дроп: %s",
                    user.getUserName(), monster.getMonsterTemplate().getName(), loot);
        } else {
            // Монстр ранен
            monster.setCurrentHp(newHp);
            monstersDungeonService.update(monster);
            return String.format("%s атаковал %s. У монстра осталось %d HP",
                    user.getUserName(), monster.getMonsterTemplate().getName(), newHp);
        }
    }

    private String upDrop(User user, List<Item> items) {
        items.forEach(item -> userItemService.add(user, item));
        return userItemService.inventory(user);
    }

}
