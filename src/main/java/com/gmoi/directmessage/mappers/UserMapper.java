package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User user);

    List<UserDTO> toDto(List<User> users);

    User toEntity(UserDTO userDTO);

    List<User> toEntity(List<UserDTO> userDTOS);

}