package com.gmoi.directmessage.entities.attachment;

import com.gmoi.directmessage.aws.S3Service;
import com.gmoi.directmessage.mappers.AttachmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;

    public AttachmentDTO uploadFile(MultipartFile file) {

        Attachment attachment = Attachment.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .size(file.getSize()).
                build();

        Attachment savedAttachment = attachmentRepository.save(attachment);
        s3Service.uploadFile(savedAttachment.getId().toString(), file);

        return AttachmentMapper.INSTANCE.toDto(savedAttachment);
    }

    public ResponseEntity<byte[]> downloadFile(Long id) {

        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        try (InputStream inputStream = s3Service.getFile(attachment.getId().toString())) {
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

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .header("Content-Type", contentType)
                    .body(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public void deleteFile(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
        s3Service.deleteFile(attachment.getId().toString());
        attachmentRepository.delete(attachment);
    }
}
