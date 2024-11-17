package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.message.MessageDTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO toDto(Message user);

    List<MessageDTO> toDto(List<Message> users);
}
