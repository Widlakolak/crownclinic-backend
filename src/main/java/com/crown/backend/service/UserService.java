package com.crown.backend.service;

import com.crown.backend.domain.User;
import com.crown.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updatedUser.getFirstName());
                    existing.setLastName(updatedUser.getLastName());
                    existing.setEmail(updatedUser.getEmail());
                    existing.setPhone(updatedUser.getPhone());
                    if (updatedUser.getRole() != null) {
                        existing.setRole(updatedUser.getRole());
                    }
                    if (updatedUser.getGoogleCalendarId() != null) {
                        existing.setGoogleCalendarId(updatedUser.getGoogleCalendarId());
                    }
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        String email = auth.getName();
        return userRepository.findByEmail(email);
    }

    public User partialUpdate(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Object firstName = updates.get("firstName");
        if (firstName instanceof String s) {
            user.setFirstName(s);
        }

        Object lastName = updates.get("lastName");
        if (lastName instanceof String s) {
            user.setLastName(s);
        }

        Object phone = updates.get("phone");
        if (phone instanceof String s) {
            user.setPhone(s);
        }

        Object email = updates.get("email");
        if (email instanceof String s) {
            user.setEmail(s);
        }

        Object role = updates.get("role");
        if (role instanceof User.Role r) {
            user.setRole(r);
        }

        Object googleCalendarId = updates.get("googleCalendarId");
        if (googleCalendarId instanceof String s) {
            user.setGoogleCalendarId(s);
        }

//  Zmiana hasła
//        Object password = updates.get("password");
//        if (password instanceof String s) {
//            user.setPassword(passwordEncoder.encode(s));
//        }

//        Object passwordConfirmation = updates.get("passwordConfirmation");
//        if (passwordConfirmation instanceof String s) {
//            user.setPassword(passwordEncoder.encode(s));
//        }

        return userRepository.save(user);
    }
}