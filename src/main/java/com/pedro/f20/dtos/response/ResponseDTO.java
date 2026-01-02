package com.pedro.f20.dtos.response;

public record ResponseDTO (
    String message,
    Object data,
    Integer status
) {}
