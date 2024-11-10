package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.user.User;
import com.gmoi.directmessage.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);
}