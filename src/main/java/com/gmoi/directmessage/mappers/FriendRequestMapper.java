package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.entities.friendrequest.FriendRequest;
import com.gmoi.directmessage.entities.friendrequest.FriendRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface FriendRequestMapper {

    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    FriendRequestDTO toDto(FriendRequest user);

    List<FriendRequestDTO> toDto(List<FriendRequest> users);

}
