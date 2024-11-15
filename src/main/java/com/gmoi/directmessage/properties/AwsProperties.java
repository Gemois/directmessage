package com.gmoi.directmessage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private S3 s3 = new S3();
    private String region;
    private String accessKeyId;
    private String secretAccessKey;

    @Data
    public static class S3 {
        private String bucketName;
    }
}