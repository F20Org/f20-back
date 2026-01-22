package com.pedro.f20.jobs.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.f20.dtos.mail.MailJob;

import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.TimeUnit;

@Component
public class MailProcess {

    private final SpringTemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final JavaMailSender mailSender;
    private static final String QUEUE_NAME = "email_queue";

    public MailProcess(StringRedisTemplate redisTemplate, ObjectMapper objectMapper, JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Scheduled(fixedDelay = 100)
    public void processQueue() {
        String json = redisTemplate.opsForList().leftPop(QUEUE_NAME, 2, TimeUnit.SECONDS);

        if (json != null) {
            try {
                MailJob job = objectMapper.readValue(json, MailJob.class);
                System.out.println(">>> Sending email to: " + job.to());

                ClassPathResource logo = new ClassPathResource("static/images/logo.png");
                ClassPathResource icon = new ClassPathResource("static/images/dice.png");

                Context context = new Context();
                context.setVariable("messageBody", job.body());
                context.setVariable("username", job.username());
                String htmlContent = templateEngine.process("mail-template", context);

                MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(job.to());
                helper.setSubject(job.subject());
                helper.setText(htmlContent, true);
                helper.setFrom("pedrohvidals@gmail.com");
                helper.addInline("logo", logo);
                helper.addInline("icon", icon);

                mailSender.send(message);

                System.out.println(">>> Email sent successfully!");
                
            } catch (Exception e) {
                System.err.println("Error processing job: " + json);
            }
        }
    }
}