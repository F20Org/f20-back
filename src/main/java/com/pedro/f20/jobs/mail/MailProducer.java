package com.pedro.f20.jobs.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.mail.MailJob;

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

    public void sendEmail(String to, String subject, String body) {
        try {
            MailJob job = new MailJob(to, subject, body);
            String json = objectMapper.writeValueAsString(job);
            
            redisTemplate.opsForList().rightPush(QUEUE_NAME, json);
            System.out.println("Job added to Redis: " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendWelcomeEmail(UserDataComplete user) {
        String subject = "Welcome to F20, " + user.username() + "!";
        String body = "Hello " + user.username() + ",\n\nThank you for registering at F20.\n\nBest regards,\nF20 Team";
        sendEmail(user.email(), subject, body);
    }
}