package org.myShortLink.admin.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user")
@Data
public class User {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator")
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.admin.config.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(length = 256, nullable = false, unique = true, updatable = false)
    private String username;

    @Column(length = 256)
    private String password;

    @Column(length = 512, unique = true)
    private String email;

    @Column(length = 100, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime archivedAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
