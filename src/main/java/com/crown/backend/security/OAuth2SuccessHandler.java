package com.crown.backend.security;

import com.crown.backend.domain.User;
import com.crown.backend.repository.UserRepository;
import com.crown.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OAuth2AuthorizedClientService clientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        Authentication auth) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) auth;

        String email = Optional.ofNullable((String) oauthToken.getPrincipal().getAttribute("email")).orElse("");
        String name = Optional.ofNullable((String) oauthToken.getPrincipal().getAttribute("name")).orElse("");
        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        var client = clientService.loadAuthorizedClient("google", oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        User user = userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setGoogleAccessToken(accessToken);
                    existing.setFirstName(existing.getFirstName() != null ? existing.getFirstName() : firstName);
                    existing.setLastName(existing.getLastName() != null ? existing.getLastName() : lastName);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .role(User.Role.DOCTOR)
                        .googleCalendarId("primary")
                        .googleAccessToken(accessToken)
                        .build()));

        user.setGoogleAccessToken(accessToken);
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        String path = buildRedirectPath(user);
        String jwtTokenEncoded = URLEncoder.encode(jwt, StandardCharsets.UTF_8);
        String redirectUrl = "http://localhost:8081/" +
                URLEncoder.encode(path, StandardCharsets.UTF_8) +
                "?token=" + jwtTokenEncoded;

        resp.sendRedirect(redirectUrl);
    }

    private String buildRedirectPath(User user) {
        if (user.getFirstName()==null||user.getLastName()==null||user.getPhone()==null) {
            return "profile";
        }
        return switch (user.getRole()) {
            case ADMIN -> "admin";
            case RECEPTIONIST -> "reception";
            default -> "doctor";
        };
    }
}
