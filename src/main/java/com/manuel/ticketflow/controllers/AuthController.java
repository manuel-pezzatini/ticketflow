package com.manuel.ticketflow.controllers;

import com.manuel.ticketflow.services.UtenteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.ticketflow.dto.LoginRequest;
import com.manuel.ticketflow.dto.LoginResponse;
import com.manuel.ticketflow.dto.RegisterRequest;
import com.manuel.ticketflow.dto.RegisterResponse;
import com.manuel.ticketflow.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtenteService utenteService;
    private final JwtService jwtService;

    AuthController(UtenteService utenteService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.utenteService = utenteService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public RegisterResponse registraUtente(@Valid @RequestBody RegisterRequest registerRequest){
        return utenteService.registraUtente(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse loginUtente(@Valid @RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        LoginResponse response = utenteService.getLoginResponse(loginRequest.getEmail());
        String token = jwtService.generateToken(loginRequest.getEmail());
        response.setToken(token);

        return response;
    }
    
}
