package com.example.demo.config;


import com.example.demo.security.CustomDetailService;
import com.example.demo.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomDetailService customDetailService;
    private final PasswordConfig passwordConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider, JwtAuthentificationFilter jwtAuthentificationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/leaderboard", "/api/bestiary/{id}", "/api/bestiary").permitAll()
                        .requestMatchers("/api/bestiary/**", "/api/dungeons").hasRole("MASTER")
                        .requestMatchers(HttpMethod.DELETE, "/api/players/**", "/api/bestiary/**")
                        .hasRole("ADMIN").anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customDetailService);
        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
        return provider;
    }

    @Bean
    public JwtAuthentificationFilter jwtAuthentificationFilter(JwtService jwtService, CustomDetailService customDetailService) {
        return new JwtAuthentificationFilter(jwtService, customDetailService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
