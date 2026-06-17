package com.example.demo.repository;

import com.example.demo.domain.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BestiaryRepository extends JpaRepository<Monster, Long> {
   boolean existsByName(String name);
}
