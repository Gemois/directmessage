package com.gmoi.directmessage.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmoi.directmessage.auth.dtos.AuthenticationRequest;
import com.gmoi.directmessage.auth.dtos.AuthenticationResponse;
import com.gmoi.directmessage.auth.dtos.RegisterRequest;
import com.gmoi.directmessage.mail.MailService;
import com.gmoi.directmessage.models.User;
import com.gmoi.directmessage.models.UserRole;
import com.gmoi.directmessage.models.UserStatus;
import com.gmoi.directmessage.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationService confirmationTokenService;

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Attempt to register with an already existing email: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email " + request.getEmail() + " already exists.");
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .status(UserStatus.ONLINE)
                .lastActivityDate(LocalDateTime.now())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(user);
        mailService.sendRegistrationSuccessEmail(savedUser);

        String token = confirmationTokenService.createConfirmationToken(user);
        mailService.sendConfirmationEmail(savedUser, confirmationTokenService.buildVerificationLink(token));

        log.info("User registration successful: {}", savedUser.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found during authentication: {}", request.getEmail());
                    return new UsernameNotFoundException("User with email " + request.getEmail() + " not found.");
                });

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        log.info("Authentication successful for user: {}", request.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Refreshing token");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {

                Boolean twoFactorCompleted = jwtService.extractClaim(refreshToken, claims ->
                        claims.get("twoFactorCompleted", Boolean.class));

                var accessToken = jwtService.generateToken(user, twoFactorCompleted);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                log.debug("Generated new access token for user: {}", userEmail);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}