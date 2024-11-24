package com.gmoi.directmessage.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmoi.directmessage.auth.confirmation.ConfirmationToken;
import com.gmoi.directmessage.auth.confirmation.ConfirmationTokenService;
import com.gmoi.directmessage.auth.tfa.QRCodeService;
import com.gmoi.directmessage.auth.tfa.TwoFactorAuthService;
import com.gmoi.directmessage.auth.tfa.TwoFactorResponse;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserRepository;
import com.gmoi.directmessage.entities.user.UserRole;
import com.gmoi.directmessage.mail.MailService;
import com.gmoi.directmessage.utils.RequestUtil;
import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
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
    private final QRCodeService qrCodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorAuthService twoFactorAuthService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;

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
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                log.debug("Generated new access token for user: {}", userEmail);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not valid"));

        if (confirmationToken.getConfirmedAt() != null)
            throw new IllegalStateException("email already confirmed");

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
            throw new IllegalStateException("token expired");

        confirmationTokenService.setConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setActivated(true);
        userRepository.save(user);
        log.info("User {} has been successfully activated.", confirmationToken.getUser().getUsername());
    }

    public void sendConfirmationEmail() {
        User user = RequestUtil.getCurrentUser();

        if (user.isActivated())
            throw new IllegalStateException("user is already activated!");

        confirmationTokenService.retireTokens(user);
        String token = confirmationTokenService.createConfirmationToken(user);
        mailService.sendConfirmationEmail(user, confirmationTokenService.buildVerificationLink(token));
    }


    public TwoFactorResponse enable2FA() {
        GoogleAuthenticatorKey key = twoFactorAuthService.generateSecretKey();

        User user = RequestUtil.getCurrentUser();
        user.setTwoFactorSecret(key.getKey());
        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        String qrCodeUrl = twoFactorAuthService.generateQRCode(key, user);
        String encodedQRCode;
        try {
            encodedQRCode = qrCodeService.generateQRCode(qrCodeUrl);
        } catch (WriterException e) {
            throw new IllegalStateException("QR code generation failed", e);
        }

        return TwoFactorResponse.builder()
                .secretKey(key.getKey())
                .qrCodeImage(encodedQRCode)
                .qrCodeUrl(qrCodeUrl)
                .build();
    }

    public void verify2FA(int otp) {
        User user = RequestUtil.getCurrentUser();
        String secretKey = user.getTwoFactorSecret();

        boolean isOtpValid = twoFactorAuthService.validateOTP(secretKey, otp);
        if (!isOtpValid) {
            throw new RuntimeException("Invalid OTP");
        }

        userRepository.save(user);
    }
}