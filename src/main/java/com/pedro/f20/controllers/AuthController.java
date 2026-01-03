package com.pedro.f20.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserRegisterDTO;
import com.pedro.f20.dtos.response.ResponseDTO;
import com.pedro.f20.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserRegisterDTO data) {
        UserRegisterDTO processedData = new UserRegisterDTO(
            data.username(),
            data.email(),
            data.password()
        );

        UserDataComplete userCreated = service.create(processedData);
        return ResponseEntity.ok().body(new ResponseDTO(
            "User created successfully",
            userCreated,
            201
        ));
    }
}