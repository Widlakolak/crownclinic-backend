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
import java.util.Map;

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

        var oauth = (OAuth2AuthenticationToken) auth;
        String email = oauth.getPrincipal().getAttribute("email");
        String name = oauth.getPrincipal().getAttribute("name");

        // Pobierz dostęp token
        var client = clientService.loadAuthorizedClient("google", oauth.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .firstName(name)
                        .role(User.Role.DOCTOR)
                        .googleCalendarId("primary") // Standardowy kalendarz
                        .build()));

        user.setGoogleAccessToken(accessToken); // jeśli chcesz przechowywać
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        resp.sendRedirect("http://localhost:8081/oauth-success?token=" + jwt);
    }
}
