package com.gmoi.directmessage.utils;

import com.gmoi.directmessage.models.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@UtilityClass
public class SessionUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new EntityNotFoundException("No authenticated user found");
        }

        return (User) authentication.getPrincipal();
    }

    public static User getCurrentUser(Principal principal) {

        if (principal instanceof UsernamePasswordAuthenticationToken authToken) {
            return (User) authToken.getPrincipal();
        }

        throw new EntityNotFoundException("No authenticated user found");
    }
}
