package com.pedro.f20.jobs.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.mail.MailJob;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MailProducer {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String QUEUE_NAME = "email_queue";

    public MailProducer(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEmail(String username, String to, String subject, String body, Optional<String> verificationCode) {
        try {
            MailJob job = new MailJob(username, to, subject, body, verificationCode.orElse(null));
            String json = objectMapper.writeValueAsString(job);
            
            redisTemplate.opsForList().rightPush(QUEUE_NAME, json);
            System.out.println("Job added to Redis: " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendWelcomeEmail(UserDataComplete user) {
        String subject = "Welcome to F20!";
        String body = "Hello " + user.username() + ",\n\nThank you for registering at F20.";
        sendEmail(user.username(), user.email(), subject, body, Optional.empty());
    }

    public void sendEmailVerificationCode(UserDataComplete user, String code) {
        String subject = "Verify your email for F20!";
        String body = "Hello " + user.username() + ",\n\nPlease use the following code to verify your email address.";
        sendEmail(user.username(), user.email(), subject, body, Optional.of(code));
    }

    public void sendEmailVerifiedConfirmation(UserDataComplete user) {
        String subject = "Your email has been verified!";
        String body = "Hello " + user.username() + ",\n\nYour email address has been successfully verified.";
        sendEmail(user.username(), user.email(), subject, body, Optional.empty());
    }
}