package com.pedro.f20.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserLoginDTO;
import com.pedro.f20.dtos.auth.UserRegisterDTO;
import com.pedro.f20.dtos.auth.ValidadeEmailDTO;
import com.pedro.f20.dtos.response.ResponseDTO;
import com.pedro.f20.entities.User;
import com.pedro.f20.services.AuthService;
import com.pedro.f20.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final TokenService tokenService;
    private final AuthenticationManager manager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthService service, AuthenticationManager manager, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.service = service;
        this.manager = manager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid UserLoginDTO data) {

        var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = manager.authenticate(token);

        User authenticatedUser = (User) auth.getPrincipal();
        var jwtToken = tokenService.generateToken(authenticatedUser);

        return ResponseEntity.ok().body(new ResponseDTO(
            "Login successful",
            jwtToken,
            200
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserRegisterDTO data) {
        UserRegisterDTO processedData = new UserRegisterDTO(
            data.username(),
            data.email(),
            passwordEncoder.encode(data.password())
        );

        UserDataComplete userCreated = service.create(processedData);
        return ResponseEntity.ok().body(new ResponseDTO(
            "User created successfully",
            userCreated,
            201
        ));
    }

    @PutMapping("/verify-email")
    public ResponseEntity<ResponseDTO> verifyEmail(@RequestBody @Valid ValidadeEmailDTO data) {
        service.verifyEmail(data);
        return ResponseEntity.ok().body(new ResponseDTO(
            "Email verified successfully",
            null,
            200
        ));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> testAuth() {
        return ResponseEntity.ok().body(new ResponseDTO(
            "Authenticated access successful",
            null,
            200
        ));
    }
}