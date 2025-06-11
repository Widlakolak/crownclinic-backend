package com.crown.backend.controller;

import com.crown.backend.dto.AuthRequest;
import com.crown.backend.dto.AuthResponse;
import com.crown.backend.dto.GoogleLoginRequest;
import com.crown.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        AuthResponse response = authenticationService.authenticateWithGoogle(googleLoginRequest.code());
        return ResponseEntity.ok(response);
    }
}