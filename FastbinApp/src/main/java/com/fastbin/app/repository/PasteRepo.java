package com.fastbin.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastbin.app.entity.Paste;
import com.fastbin.app.entity.User;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;


public interface PasteRepo extends JpaRepository<Paste,Long> {

    Paste findByShortCode(String shortCode);

    List<Paste> findByUserId(Long userId);

    List<Paste> findByExpirationDateBefore(LocalDateTime expirationDate);

    @Transactional
    void deleteByShortCode(String shortCode);

}