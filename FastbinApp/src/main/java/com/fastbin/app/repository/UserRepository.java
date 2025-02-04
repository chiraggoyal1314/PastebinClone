package com.fastbin.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastbin.app.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}