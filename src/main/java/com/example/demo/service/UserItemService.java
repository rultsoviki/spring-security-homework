package com.example.demo.service;

import com.example.demo.domain.Item;
import com.example.demo.domain.User;
import com.example.demo.domain.UserItems;
import com.example.demo.repository.UserItemsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserItemService {
    private UserItemsRepository userItemsRepository;

    public void add(User user, Item item) {
        var userItem = userItemsRepository.findByUserAndItem(user, item);
        if (userItem.isEmpty()) {
            UserItems userItems = new UserItems(null, user, item, 1);
            userItemsRepository.save(userItems);
        } else {
            UserItems items = userItem.get();
            items.setQuantity(items.getQuantity() + 1);
            userItemsRepository.save(items);
        }
    }

    public String inventory(User user) {
        return userItemsRepository.findByUser(user).stream()
                .map(userItems ->
                        userItems.getItem().getName() + " количестко " + userItems.getQuantity())
                .collect(Collectors.joining(" ,"));

    }

}
