package com.gmoi.directmessage.auth.services;

import com.gmoi.directmessage.auth.models.ConfirmationToken;
import com.gmoi.directmessage.auth.repositories.ConfirmationTokenRepository;
import com.gmoi.directmessage.mail.MailService;
import com.gmoi.directmessage.models.User;
import com.gmoi.directmessage.properties.GeneralProperties;
import com.gmoi.directmessage.properties.UserProperties;
import com.gmoi.directmessage.repositories.UserRepository;
import com.gmoi.directmessage.utils.SessionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final GeneralProperties generalProperties;
    private final UserProperties userProperties;
    private final UserRepository userRepository;
    private final MailService mailService;

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not valid"));

        if (confirmationToken.getConfirmedAt() != null)
            throw new IllegalStateException("email already confirmed");

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
            throw new IllegalStateException("token expired");

        setConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setEmailVerified(true);

        userRepository.save(user);
        log.info("User {} has been successfully activated.", confirmationToken.getUser().getUsername());
    }

    public void resendToken() {
        User user = SessionUtil.getCurrentUser();

        if (user.isEmailVerified()) {
            throw new IllegalStateException("email is already verified");
        }

        retireTokens(user);
        String token = createConfirmationToken(user);

        mailService.sendConfirmationEmail(user, buildVerificationLink(token));
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public void retireTokens(User user) {
        confirmationTokenRepository.retireTokens(user, LocalDateTime.now());
    }

    public String createConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .expiresAt(LocalDateTime.now().plusMinutes(userProperties.getConfirmationToken().getExpirationMinutes()))
                .token(token)
                .user(user)
                .build();

        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public String buildVerificationLink(String token) {
        String baseUrl = "http://" + generalProperties.getHost() + (generalProperties.getPort()  != 0 ? ":" + generalProperties.getPort() : "");
        return  baseUrl + "/api/v1/auth/email/confirm?token=" + token;
    }
}