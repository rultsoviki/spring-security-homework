package com.example.demo.repository;

import com.example.demo.domain.Dungeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DungeonRepository extends JpaRepository<Dungeon, Long> {

    Optional<Dungeon> findById(Long id);

    @Query("select d from Dungeon d left join fetch d.monsters dm left join fetch dm.monsterTemplate where d.id = :id")
    Optional<Dungeon> findByIdWithMonsters(@Param("id") Long id);

    boolean existsByName(String name);
}
