package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.dtos.MessageDTO;
import com.gmoi.directmessage.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO toDto(Message message);

    List<MessageDTO> toDto(List<Message> messages);

    Message toEntity(MessageDTO messageDTO);

    List<Message> toEntity(List<MessageDTO> messageDTOS);
}
