package com.example.demo.service;

import com.example.demo.controllers.dto.UserRequest;
import com.example.demo.controllers.dto.UserResponse;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.vo.RoleName;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private UserMapper mapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserResponse save(UserRequest userRequest) {
        var user = mapper.toEntity(userRequest);
        if (!userRepository.existsByUserName(userRequest.name())) {

            var password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));

            Role defaultRole = roleRepository.findByName(RoleName.ROLE_PLAYER.getName())
                    .orElseThrow(() -> new RuntimeException("Роль по умолчанию не найдена"));
            user.setRoles(Set.of(defaultRole));

            userRepository.save(user);

            return mapper.toResponse(user);
        }
        throw new RuntimeException("Пользователь с таким именем уже есть");
    }

    public List<User> topUsers() {
        return userRepository.topUsers();
    }

    public User getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.findByUserName(name).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
        public void banUser(Long id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            user.setBanned(true);
            userRepository.save(user);
        }

        public boolean inBan(String login){
        return userRepository.findBannedByUserName(login);}
    }


