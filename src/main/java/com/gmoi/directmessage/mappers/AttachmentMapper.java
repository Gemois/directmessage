package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.entities.attachment.Attachment;
import com.gmoi.directmessage.entities.attachment.AttachmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AttachmentMapper {

    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    AttachmentDTO toDto(Attachment user);

    List<AttachmentDTO> toDto(List<Attachment> users);

}