package com.gmoi.directmessage.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Auditable implements UserDetails {
    @Id
    @GeneratedValue()
    private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private boolean emailVerified;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isActivated;

    private String twoFactorSecret;
    private boolean twoFactorEnabled;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    private LocalDateTime lastActivityDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
