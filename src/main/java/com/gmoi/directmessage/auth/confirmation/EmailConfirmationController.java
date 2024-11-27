package com.gmoi.directmessage.auth.confirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/email")
public class EmailConfirmationController {

    private final ConfirmationService emailConfirmationTokenService;

    @GetMapping("/confirm")
    public ModelAndView confirm(@RequestParam("token") String token) {
        try {
            emailConfirmationTokenService.confirmToken(token);
        } catch (Exception e) {
            return new ModelAndView("email-confirmation-error");
        }
        return  new ModelAndView("email-confirmation-success");
    }

    @GetMapping("/re-send")
    public ResponseEntity<Void> sendConfirmationLink() {
        emailConfirmationTokenService.resendToken();
        return ResponseEntity.ok().build();
    }

}
