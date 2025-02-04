package com.fastbin.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Paste {
    // private static final Logger log = LoggerFactory.getLogger(Paste.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pasteId;

    @Column(unique = true)
    private String shortCode; // The shortened code (e.g., "abc123")


    private String title;

    @Column(length = 100000)
    private String content;

   @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;

    private LocalDateTime expirationDate;

    // @ManyToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    // private User user;

    private Long userId;

    // @PrePersist
    // public void prePersist() {
    //     log.info("PrePersist: Setting createdAt to {}", LocalDateTime.now());
    // }

    // @PreUpdate
    // public void preUpdate() {
    //     log.info("PreUpdate: Setting lastModifiedAt to {}", LocalDateTime.now());
    // }
    // Constructors, getters, setters
}