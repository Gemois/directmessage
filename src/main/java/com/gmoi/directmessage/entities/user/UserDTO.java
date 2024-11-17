package com.gmoi.directmessage.entities.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserStatus status;
    private byte[] photo;
}
