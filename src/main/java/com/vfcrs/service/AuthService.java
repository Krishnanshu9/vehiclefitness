package com.vfcrs.service;

import com.vfcrs.config.JwtUtils;
import com.vfcrs.dto.AuthResponse;
import com.vfcrs.dto.LoginRequest;
import com.vfcrs.dto.RegisterRequest;
import com.vfcrs.entity.User;
import com.vfcrs.exception.EmailAlreadyExistsException;
import com.vfcrs.exception.InvalidCredentialsException;
import com.vfcrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setRole("USER"); // Set default role

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
    
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    
        String token = jwtUtils.generateToken(user); // Now accepts User object
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }
}