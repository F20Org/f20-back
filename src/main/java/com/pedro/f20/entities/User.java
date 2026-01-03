package com.pedro.f20.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserRegisterDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tb_users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    private LocalDateTime dtcreate;
    private LocalDateTime dtupdate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmailCode emailCode;

    public User(UserRegisterDTO data) {
        this.username = data.username();
        this.email = data.email();
        this.password = data.password();
        this.isActive = true;
        this.isEmailVerified = false;
        this.dtcreate = LocalDateTime.now();
        this.dtupdate = LocalDateTime.now();
    }

    public UserDataComplete toDto() {
        return new UserDataComplete(
            this.id,
            this.username,
            this.email,
            this.isActive
        );
    }

    public String getName() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

}
