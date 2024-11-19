package com.gmoi.directmessage.aws;

import com.gmoi.directmessage.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final AwsProperties awsS3Properties;

    public void uploadFile(String key, MultipartFile file) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(awsS3Properties.getS3().getBucketName())
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public InputStream getFile(String key) {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(awsS3Properties.getS3().getBucketName())
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch file", e);
        }
    }

    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(awsS3Properties.getS3().getBucketName())
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}