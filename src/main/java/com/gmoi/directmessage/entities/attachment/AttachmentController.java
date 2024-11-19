package com.gmoi.directmessage.entities.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping()
    public ResponseEntity<AttachmentDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachmentService.uploadFile(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        return attachmentService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        attachmentService.deleteFile(id);
    }
}
