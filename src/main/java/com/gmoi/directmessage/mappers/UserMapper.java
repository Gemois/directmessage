package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);
}