package com.gmoi.directmessage.auth.confirmation;

import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.properties.GeneralProperties;
import com.gmoi.directmessage.properties.UserProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final GeneralProperties generalProperties;
    private final UserProperties userProperties;

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
        return  baseUrl + "/confirm?token=" + token;
    }
}