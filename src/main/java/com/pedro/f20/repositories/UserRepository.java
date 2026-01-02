package com.pedro.f20.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedro.f20.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
