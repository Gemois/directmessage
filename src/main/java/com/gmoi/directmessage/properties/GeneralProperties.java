package com.gmoi.directmessage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "general")
public class GeneralProperties {

    private Messages messages = new Messages();
    private Users users = new Users();

    @Data
    public static class Messages {
        private long editMinutes;
        private long deleteMinutes;
    }

    @Data
    public static class Users {

        private Status status = new Status();
        private Admin admin = new Admin();
        private ConfirmationToken confirmationToken = new ConfirmationToken();

        @Data
        public static class Status {
            private long inactiveThresholdMinutes;
        }

        @Data
        public static class Admin {
            private String email;
            private String password;
        }

        @Data
        public static class ConfirmationToken {
            private long expirationMinutes;
        }
    }
}
