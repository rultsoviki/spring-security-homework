package com.example.demo.service;

import com.example.demo.domain.Monster;
import com.example.demo.domain.MonstersDongeons;
import com.example.demo.repository.MonstersDongeonsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MonstersDungeonService {
    private MonstersDongeonsRepository monstersDongeonsRepository;

    public MonstersDongeons findById(Long id) {
        return monstersDongeonsRepository.findById(id).get();
    }

    public MonstersDongeons update(MonstersDongeons monstersDongeons) {
        return monstersDongeonsRepository.save(monstersDongeons);
    }

    public void delete(MonstersDongeons monstersDongeons){
        monstersDongeonsRepository.delete(monstersDongeons);
    }
}
