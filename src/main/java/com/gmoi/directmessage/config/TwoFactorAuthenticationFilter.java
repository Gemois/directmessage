package com.gmoi.directmessage.config;

import com.gmoi.directmessage.auth.services.JwtService;
import com.gmoi.directmessage.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class TwoFactorAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean has2fAEnabled = ((User) authentication.getPrincipal()).isTwoFactorEnabled();

        if (has2fAEnabled && checkTwoFactorStatus(authentication)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Two-factor authentication required.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkTwoFactorStatus(Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        return jwtService.extractClaim(jwtToken, claims -> claims.get("twoFactorComplete", Boolean.class));
    }
}
