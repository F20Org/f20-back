package com.pedro.f20.jobs.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.f20.dtos.mail.MailJob;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class MailProcess {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final JavaMailSender mailSender;
    private static final String QUEUE_NAME = "email_queue";

    public MailProcess(StringRedisTemplate redisTemplate, ObjectMapper objectMapper, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedDelay = 100)
    public void processQueue() {
        String json = redisTemplate.opsForList().leftPop(QUEUE_NAME, 2, TimeUnit.SECONDS);

        if (json != null) {
            try {
                MailJob job = objectMapper.readValue(json, MailJob.class);
                System.out.println(">>> Sending email to: " + job.to());

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(job.to());
                message.setSubject(job.subject());
                message.setText(job.body());
                message.setFrom("pedrohvidals@gmail.com");
                mailSender.send(message);

                System.out.println(">>> Email sent successfully!");
                
            } catch (Exception e) {
                System.err.println("Error processing job: " + json);
            }
        }
    }
}