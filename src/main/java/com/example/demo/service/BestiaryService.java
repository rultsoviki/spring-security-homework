package com.example.demo.service;

import com.example.demo.domain.Monster;
import com.example.demo.repository.BestiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BestiaryService {
    private final BestiaryRepository bestiaryRepository;

    public List<Monster> allMonsters() {
        return bestiaryRepository.findAll();
    }

    public Monster findById(Long id) {
        return bestiaryRepository.findById(id).orElseThrow(() -> new RuntimeException("Такова монстра нету"));
    }

    public boolean create(Monster monster) {
        if (!bestiaryRepository.existsByName(monster.getName())) {
            bestiaryRepository.save(monster);
            return true;
        }
        return false;
    }

    public Monster update(Monster monster){
        return bestiaryRepository.save(monster);
    }

    public void delete(Long id){
        bestiaryRepository.deleteById(id);
    }
}
