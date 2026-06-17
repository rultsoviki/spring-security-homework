package com.example.demo;

import com.example.demo.domain.Role;
import com.example.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestDataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder()
                            .name("ROLE_PLAYER")
                    .build());
            roleRepository.save(Role.builder()
                    .name("ROLE_ADMIN")
                    .build());
            roleRepository.save(Role.builder()
                    .name("ROLE_MASTER")
                    .build());
        }
    }
}
