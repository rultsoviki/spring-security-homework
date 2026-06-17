package com.example.demo.service;

import com.example.demo.controllers.dto.AuthResponse;
import com.example.demo.controllers.dto.UserRequest;
import com.example.demo.security.CustomDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthService {
    private JwtService jwtService;
    private CustomDetailService customDetailService;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    public AuthResponse login(UserRequest userRequest) {
        var authentic = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.name(),
                        userRequest.password()));

        UserDetails userDetails = (UserDetails) authentic.getPrincipal();

        if (userService.inBan(userDetails.getUsername())) {
            throw new RuntimeException("Вы забанены");

        }        return new AuthResponse(
                jwtService.generateAccessToken(userDetails),
                jwtService.generatedRefreshToken(userDetails));
    }

    public AuthResponse refresh(String refreshToken){
        String userName = jwtService.extractName(refreshToken);
        UserDetails userDetails = customDetailService.loadUserByUsername(userName);

        if(!jwtService.isRefreshTokenValid(refreshToken,userDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"invalid_refresh_token");
        }
        jwtService.invalidateToken(refreshToken);

        return new AuthResponse(jwtService.generateAccessToken(userDetails), jwtService.generatedRefreshToken(userDetails));

    }
}
