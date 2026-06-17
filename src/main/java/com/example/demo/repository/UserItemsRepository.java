package com.example.demo.repository;

import com.example.demo.domain.Item;
import com.example.demo.domain.User;
import com.example.demo.domain.UserItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemsRepository extends JpaRepository<UserItems,Long> {

    Optional<UserItems> findByUserAndItem(User user, Item item);

    List<UserItems> findByUser(User user);
}
