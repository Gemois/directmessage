package com.gmoi.directmessage.mappers;

import com.gmoi.directmessage.models.FriendRequest;
import com.gmoi.directmessage.dtos.FriendRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FriendRequestMapper {

    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    FriendRequestDTO toDto(FriendRequest friendRequest);

    List<FriendRequestDTO> toDto(List<FriendRequest> friendRequests);

    FriendRequest toEntity(FriendRequestDTO friendRequestDTO);

    List<FriendRequest> toEntity(List<FriendRequestDTO> friendRequestDTOS);
}
