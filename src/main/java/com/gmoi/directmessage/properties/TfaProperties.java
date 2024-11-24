package com.gmoi.directmessage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "tfa")
public class TfaProperties {
    private String issuer;
}
