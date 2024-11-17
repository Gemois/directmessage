package com.gmoi.directmessage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "user")
public class UserProperties {

    private status status = new status();

    @Data
    public static class status {
        private long inactiveThresholdMinutes;
    }
}
