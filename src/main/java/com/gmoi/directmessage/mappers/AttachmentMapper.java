package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.dtos.AttachmentDTO;
import com.gmoi.directmessage.models.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AttachmentMapper {

    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    AttachmentDTO toDto(Attachment attachment);

    List<AttachmentDTO> toDto(List<Attachment> attachments);

    Attachment toEntity(AttachmentDTO attachmentDTO);

    List<Attachment> toEntity(List<AttachmentDTO> attachmentDTOS);

}
