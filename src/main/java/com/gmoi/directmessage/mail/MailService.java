package com.gmoi.directmessage.mail;

import com.gmoi.directmessage.models.FriendRequest;
import com.gmoi.directmessage.models.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        log.info("Preparing to send email to: {}", to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Error occurred while sending email to: {}", to, e);
        }
    }

    public void sendRegistrationSuccessEmail(User user) {
        String subject = "Registration Successful";
        String body = MailTemplates.registrationSuccess(user.getFirstName());

        log.info("Sending registration success email to: {}", user.getEmail());
        send(user.getEmail(), subject, body);
    }

    public void sendFriendRequestEmail(FriendRequest friendRequest) {
        String subject = "New Friend Request";
        String body = MailTemplates.friendRequestSent(friendRequest.getSender().getFirstName());

        log.info("Sending friend request email to: {}", friendRequest.getRecipient().getEmail());
        send(friendRequest.getRecipient().getEmail(), subject, body);
    }

    public void sendFriendRequestAcceptedEmail(FriendRequest friendRequest) {
        String subject = "Friend Request Accepted";
        String body = MailTemplates.friendRequestAccepted(friendRequest.getRecipient().getFirstName());

        log.info("Sending friend request accepted email to: {}", friendRequest.getSender().getEmail());
        send(friendRequest.getSender().getEmail(), subject, body);
    }

    public void sendConfirmationEmail(User savedUser, String link) {
        String subject = "Verify your email";
        String body = MailTemplates.emailVerification(link);

        log.info("Sending verification email to: {}", savedUser.getFirstName());
        send(savedUser.getEmail(), subject, body);
    }
}
