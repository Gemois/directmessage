package com.gmoi.directmessage.utils;

import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RequestUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new EntityNotFoundException("No authenticated user found");
        }

        return (User) authentication.getPrincipal();
    }
}
