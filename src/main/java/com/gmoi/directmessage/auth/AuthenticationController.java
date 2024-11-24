package com.gmoi.directmessage.auth;

import com.gmoi.directmessage.auth.tfa.TwoFactorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("confirm")
    public ResponseEntity<Void> confirm(@RequestParam("token") String token) {
        service.confirmToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("confirmation-link")
    public ResponseEntity<Void> sendConfirmationLink() {
        service.sendConfirmationEmail();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<TwoFactorResponse> enable2FA() {
        return ResponseEntity.ok(service.enable2FA());
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<Void> verify2FA(@RequestParam("otp") int otp) {
        service.verify2FA(otp);
        return ResponseEntity.ok().build();
    }
}