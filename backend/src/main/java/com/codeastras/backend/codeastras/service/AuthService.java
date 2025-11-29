package com.codeastras.backend.codeastras.service;

import com.codeastras.backend.codeastras.dto.LoginRequest;
import com.codeastras.backend.codeastras.dto.SignupRequest;
import com.codeastras.backend.codeastras.entity.User;
import com.codeastras.backend.codeastras.repository.UserRepository;
import com.codeastras.backend.codeastras.config.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String signup(SignupRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("email already in use");
        }
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        String hashed = passwordEncoder.encode(req.getPassword());
        User user = new User(
                UUID.randomUUID(),
                req.getFullName(),
                req.getUsername(),
                req.getEmail(),
                hashed
        );

        userRepo.save(user);
        return jwtTokenProvider.generateToken(user.getId());
    }

    public String login(LoginRequest req) {
        Optional<User> maybe = userRepo.findByEmail(req.getEmail());
        if (maybe.isEmpty()) {
            throw new IllegalArgumentException("invalid credentials");
        }
        User user = maybe.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("invalid credentials");
        }
        return jwtTokenProvider.generateToken(user.getId());
    }
}
