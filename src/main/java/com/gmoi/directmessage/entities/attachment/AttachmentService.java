package com.gmoi.directmessage.entities.attachment;

import com.gmoi.directmessage.aws.S3Service;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserService;
import com.gmoi.directmessage.mappers.AttachmentMapper;
import com.gmoi.directmessage.utils.RequestUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final S3Service s3Service;
    private final UserService userService;
    private final AttachmentRepository attachmentRepository;

    public AttachmentDTO uploadFile(MultipartFile file) {
        log.info("Starting file upload: {}", file.getOriginalFilename());
        try {
            User user = RequestUtil.getCurrentUser();


            Attachment attachment = Attachment.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .owner(user)
                    .build();

            Attachment savedAttachment = attachmentRepository.save(attachment);
            log.info("File metadata saved with ID: {}", savedAttachment.getId());

            s3Service.uploadFile(savedAttachment.getId().toString(), file);
            log.info("File uploaded to S3 with ID: {}", savedAttachment.getId());

            userService.updateLastActivity(user);
            return AttachmentMapper.INSTANCE.toDto(savedAttachment);
        } catch (Exception e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new IllegalStateException("File upload failed", e);
        }
    }

    public ResponseEntity<byte[]> downloadFile(Long id) {
        log.info("Starting file download with ID: {}", id);
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attachment not found with ID: {}", id);
                    return new EntityNotFoundException("Attachment not found");
                });

        try (InputStream inputStream = s3Service.getFile(attachment.getId().toString())) {
            User user = RequestUtil.getCurrentUser();
            byte[] content = inputStream.readAllBytes();

            String contentType = "application/octet-stream";
            String fileType = attachment.getFileType().toLowerCase();
            if (fileType.equals("image/jpeg")) {
                contentType = "image/jpeg";
            } else if (fileType.equals("image/png")) {
                contentType = "image/png";
            } else if (fileType.endsWith(".pdf")) {
                contentType = "application/pdf";
            }
            log.info("File downloaded successfully: {}", attachment.getFileName());
            userService.updateLastActivity(user);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .header("Content-Type", contentType)
                    .body(content);
        } catch (Exception e) {
            log.error("Failed to download file with ID: {}", id, e);
            throw new IllegalStateException("Failed to download file", e);
        }
    }

    public void deleteFile(Long id) {
        log.info("Starting file deletion with ID: {}", id);
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attachment not found with ID: {}", id);
                    return new EntityNotFoundException("Attachment not found");
                });

        try {
            s3Service.deleteFile(attachment.getId().toString());
            attachmentRepository.delete(attachment);
            log.info("File deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete file with ID: {}", id, e);
            throw new IllegalStateException("Failed to delete file", e);
        }
    }
}