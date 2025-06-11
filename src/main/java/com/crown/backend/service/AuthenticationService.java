package com.crown.backend.service;

import com.crown.backend.domain.User;
import com.crown.backend.dto.AuthRequest;
import com.crown.backend.dto.AuthResponse;
import com.crown.backend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

record GoogleTokenResponse(String access_token, String id_token, String scope, String token_type, Integer expires_in) {}

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final WebClient.Builder webClientBuilder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri:postmessage}")
    private String googleRedirectUri;

    public AuthResponse authenticate(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse authenticateWithGoogle(String code) {
        GoogleTokenResponse tokenResponse = exchangeCodeForToken(code);
        GoogleIdToken.Payload payload = verifyAndDecodeIdToken(tokenResponse.id_token());

        User user = userRepository.findByEmail(payload.getEmail())
                .map(existingUser -> {
                    existingUser.setGoogleAccessToken(tokenResponse.access_token());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(payload.getEmail());
                    newUser.setFirstName(payload.get("given_name").toString());
                    newUser.setLastName(payload.get("family_name").toString());
                    newUser.setGoogleAccessToken(tokenResponse.access_token());
                    newUser.setRole(User.Role.DOCTOR);
                    return userRepository.save(newUser);
                });

        String jwt = jwtService.generateToken(user);

        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getRole().name());
    }

    private GoogleTokenResponse exchangeCodeForToken(String code) {
        WebClient webClient = webClientBuilder.build();
        return webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("code", code)
                        .with("client_id", googleClientId)
                        .with("client_secret", googleClientSecret)
                        .with("redirect_uri", googleRedirectUri)
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(GoogleTokenResponse.class)
                .block();
    }

    private GoogleIdToken.Payload verifyAndDecodeIdToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new IllegalArgumentException("Invalid Google ID token.");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not verify Google ID token", e);
        }
    }
}