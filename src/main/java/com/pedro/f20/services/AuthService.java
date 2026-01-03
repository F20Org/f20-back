package com.pedro.f20.services;

import org.springframework.stereotype.Service;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserRegisterDTO;
import com.pedro.f20.dtos.auth.ValidadeEmailDTO;
import com.pedro.f20.entities.EmailCode;
import com.pedro.f20.entities.User;
import com.pedro.f20.jobs.mail.MailProducer;
import com.pedro.f20.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final MailProducer mailProducer;

    public AuthService(UserRepository userRepository, MailProducer mailProducer) {
        this.userRepository = userRepository;
        this.mailProducer = mailProducer;
    }

    public UserDataComplete create(UserRegisterDTO data) {
        if (userRepository.existsByEmail(data.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User(data);

        EmailCode emailCode = new EmailCode(user);
        user.setEmailCode(emailCode);

        userRepository.save(user);

        mailProducer.sendWelcomeEmail(user.toDto());

        return user.toDto();
    }

    public void verifyEmail(ValidadeEmailDTO data) {
        User user = userRepository.findByEmail(data.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found with the provided email"));

        if (user.getIsActive()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        if (user.getEmailCode().getIsUsed()) {
            throw new IllegalArgumentException("Verification code has already been used");
        }

        if (user.getEmailCode().getEmailCode().equals(data.code()) == false) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        user.setIsActive(true);
        user.getEmailCode().setIsUsed(true);
        userRepository.save(user);
    }
}