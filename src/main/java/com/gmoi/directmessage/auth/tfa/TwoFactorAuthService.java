package com.gmoi.directmessage.auth.tfa;

import com.gmoi.directmessage.auth.AuthenticationResponse;
import com.gmoi.directmessage.auth.JwtService;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserRepository;
import com.gmoi.directmessage.properties.TfaProperties;
import com.gmoi.directmessage.utils.RequestUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private final TfaProperties tfaProperties;
    private final GoogleAuthenticator gAuth;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TwoFactorResponse enable2FA() {
        GoogleAuthenticatorKey key = generateSecretKey();

        User user = RequestUtil.getCurrentUser();
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
        User user = RequestUtil.getCurrentUser();
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
        User user = RequestUtil.getCurrentUser();

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
       return GoogleAuthenticatorQRGenerator.getOtpAuthURL(tfaProperties.getIssuer(), user.getEmail(), secretKey);
    }

    public boolean validateOTP(String secretKey, int otp) {
        return gAuth.authorize(secretKey, otp);
    }
}
