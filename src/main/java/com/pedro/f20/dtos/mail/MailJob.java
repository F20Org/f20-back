package com.pedro.f20.dtos.mail;

public record MailJob (
    String username,
    String to,
    String subject,
    String body
) {}
