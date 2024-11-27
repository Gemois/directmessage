package com.gmoi.directmessage.controllers;

import com.gmoi.directmessage.dtos.AttachmentDTO;
import com.gmoi.directmessage.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping()
    public ResponseEntity<AttachmentDTO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("messageRoom") String chatId) {
        return ResponseEntity.ok(attachmentService.uploadFile(file, chatId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        return attachmentService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        attachmentService.deleteFile(id);
    }

    @GetMapping("/shared/{chatId}")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByChatId(@PathVariable String chatId) {
        List<AttachmentDTO> attachments = attachmentService.getAttachmentsByChatId(chatId);
        return ResponseEntity.ok(attachments);
    }

}
