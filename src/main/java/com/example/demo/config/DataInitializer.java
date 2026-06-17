package com.example.demo.config;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.vo.RoleName;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@AllArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder,
                                      RoleRepository roleRepository) {
        return args -> {
            Role masterRole = roleRepository.findByName(RoleName.ROLE_MASTER.getName()).orElseGet(()
                    -> roleRepository.save(Role.builder()
                    .name(RoleName.ROLE_MASTER.getName())
                    .build()));

            if (!userRepository.existsByUserName("master")) {
                User master = User.builder()
                        .userName("master")
                        .password(passwordEncoder.encode("masterPass"))
                        .email("master@example.com")
                        .dmg(10)
                        .experience(0)
                        .roles(Set.of(masterRole))
                        .build();
                userRepository.save(master);
            }

            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN.getName()).orElseGet(()
                    -> roleRepository.save(Role.builder()
                    .name(RoleName.ROLE_ADMIN.getName())
                    .build()));
            if (!userRepository.existsByUserName("admin")) {
                User admin = User.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("adminPass"))
                        .email("admin@example.com")
                        .dmg(1000)
                        .experience(1000)
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.save(admin);
            }

        };
    }
}
