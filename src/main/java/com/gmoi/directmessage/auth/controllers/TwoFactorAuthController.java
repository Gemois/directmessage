package com.gmoi.directmessage.auth.controllers;

import com.gmoi.directmessage.auth.dtos.AuthenticationResponse;
import com.gmoi.directmessage.auth.services.TwoFactorAuthService;
import com.gmoi.directmessage.auth.dtos.TwoFactorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/2fa")
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;

    @PostMapping("/enable")
    public ResponseEntity<TwoFactorResponse> enable2FA() {
        return ResponseEntity.ok(twoFactorAuthService.enable2FA());
    }

    @PostMapping("/disable")
    public ResponseEntity<Void> disable2FA(@RequestParam("otp") int otp) {
        twoFactorAuthService.disable2FA(otp);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify2FA(@RequestParam("otp") int otp) {
        return ResponseEntity.ok(twoFactorAuthService.verify2FA(otp));
    }
}
