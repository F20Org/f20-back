package com.pedro.f20.dtos.mail;

public record MailJob (
    String to,
    String subject,
    String body
) {}
