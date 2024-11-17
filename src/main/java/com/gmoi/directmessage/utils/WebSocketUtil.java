package com.gmoi.directmessage.utils;

import com.gmoi.directmessage.entities.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class WebSocketUtil {

    public static User getCurrentUser(Principal principal) {

        if (principal instanceof UsernamePasswordAuthenticationToken authToken) {
            return (User) authToken.getPrincipal();
        }

        throw new IllegalArgumentException("Unable to extract user information from Principal");
    }
}
