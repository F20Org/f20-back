package com.pedro.f20.entities;

import java.time.LocalDateTime;

import com.pedro.f20.dtos.auth.UserDataComplete;
import com.pedro.f20.dtos.auth.UserRegisterDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    private LocalDateTime dtcreate;
    private LocalDateTime dtupdate;

    public User(UserRegisterDTO data) {
        this.username = data.username();
        this.email = data.email();
        this.password = data.password();
        this.isActive = false;
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

}
