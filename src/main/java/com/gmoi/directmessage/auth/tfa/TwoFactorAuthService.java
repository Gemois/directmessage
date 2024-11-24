package com.gmoi.directmessage.auth.tfa;

import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.properties.TfaProperties;
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

    public GoogleAuthenticatorKey  generateSecretKey() {
        return gAuth.createCredentials();
    }

    public String generateQRCode(GoogleAuthenticatorKey secretKey, User user) {
       return GoogleAuthenticatorQRGenerator.getOtpAuthURL(tfaProperties.getIssuer(), user.getEmail(), secretKey);
    }

    public boolean validateOTP(String secretKey, int otp) {
        return gAuth.authorize(secretKey, otp);
    }
}
