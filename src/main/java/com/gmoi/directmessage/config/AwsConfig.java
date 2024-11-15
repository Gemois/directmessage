package com.gmoi.directmessage.config;

import com.gmoi.directmessage.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    private final AwsProperties awsS3Properties;

    @Bean
    public S3Client s3Client() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(awsS3Properties.getAccessKeyId(), awsS3Properties.getSecretAccessKey());
        return S3Client.builder()
                .region(Region.of(awsS3Properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}