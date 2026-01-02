package com.pedro.f20.services;

import org.springframework.stereotype.Service;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserRegisterDTO;
import com.pedro.f20.entities.User;
import com.pedro.f20.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDataComplete create(UserRegisterDTO data) {
        if (userRepository.existsByEmail(data.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User(data);
        userRepository.save(user);

        return user.toDto();
    }
}