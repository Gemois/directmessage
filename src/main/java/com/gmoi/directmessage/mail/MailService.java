package com.gmoi.directmessage.mail;

import com.gmoi.directmessage.entities.friendrequest.FriendRequest;
import com.gmoi.directmessage.entities.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRegistrationSuccessEmail(User user) {
        String subject = "Registration Successful";
        String body = MailTemplates.registrationSuccess(user.getFirstName());
        send(user.getEmail(), subject, body);
    }

    public void sendFriendRequestEmail(FriendRequest friendRequest) {
        String subject = "New Friend Request";
        String body = MailTemplates.friendRequestSent(friendRequest.getSender().getFirstName());
        send(friendRequest.getRecipient().getEmail(), subject, body);
    }

    public void sendFriendRequestAcceptedEmail(FriendRequest friendRequest) {
        String subject = "Friend Request Accepted";
        String body = MailTemplates.friendRequestAccepted(friendRequest.getRecipient().getFirstName());
        send(friendRequest.getSender().getEmail(), subject, body);
    }

}
