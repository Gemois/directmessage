package com.gmoi.directmessage.auth.services;

import com.gmoi.directmessage.auth.dtos.AuthenticationResponse;
import com.gmoi.directmessage.auth.dtos.TwoFactorResponse;
import com.gmoi.directmessage.models.User;
import com.gmoi.directmessage.repositories.UserRepository;
import com.gmoi.directmessage.utils.SessionUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private static final String ISSUER = "DirectMessage";

    private final JwtService jwtService;
    private final GoogleAuthenticator gAuth;
    private final UserRepository userRepository;

    public TwoFactorResponse enable2FA() {
        GoogleAuthenticatorKey key = generateSecretKey();

        User user = SessionUtil.getCurrentUser();
        user.setTwoFactorSecret(key.getKey());
        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        String qrCodeUrl = generateQRCodeLink(key, user);

        return TwoFactorResponse.builder()
                .secretKey(key.getKey())
                .qrCodeUrl(qrCodeUrl)
                .build();
    }

    public AuthenticationResponse verify2FA(int otp) {
        User user = SessionUtil.getCurrentUser();
        String secretKey = user.getTwoFactorSecret();

        boolean isOtpValid = validateOTP(secretKey, otp);
        if (!isOtpValid) {
            throw new RuntimeException("Invalid OTP");
        }

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user, true);
        String jwtRefreshToken = jwtService.generateRefreshToken(user, true);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public void disable2FA(int otp) {
        User user = SessionUtil.getCurrentUser();

        if (!user.isTwoFactorEnabled()) {
            throw new IllegalStateException("Two-factor authentication is not enabled for this account.");
        }

        boolean isValidOtp = validateOTP(user.getTwoFactorSecret(), otp);
        if (!isValidOtp) {
            throw new IllegalArgumentException("Invalid OTP provided.");
        }

        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        userRepository.save(user);
    }


    public GoogleAuthenticatorKey  generateSecretKey() {
        return gAuth.createCredentials();
    }

    public String generateQRCodeLink(GoogleAuthenticatorKey secretKey, User user) {
       return GoogleAuthenticatorQRGenerator.getOtpAuthURL(ISSUER, user.getEmail(), secretKey);
    }

    public boolean validateOTP(String secretKey, int otp) {
        return gAuth.authorize(secretKey, otp);
    }
}
