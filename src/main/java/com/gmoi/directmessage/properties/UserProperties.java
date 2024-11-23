package com.gmoi.directmessage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "user")
public class UserProperties {

    private Status status = new Status();
    private Admin admin = new Admin();

    @Data
    public static class Status {
        private long inactiveThresholdMinutes;
    }

    @Data
    public static class Admin {
        private String email;
        private String password;
    }
}
