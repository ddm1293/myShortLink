package org.myShortLink.project.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_link")
public class Link {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator")
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.common.config.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(length = 32)
    private String gid;

    @Column(length = 128)
    private String domain;

    @Column(length = 8)
    private String shortURI;

    @Column(length = 128, unique = true)
    private String fullShortURL;

    @Column(length = 1024)
    private String originalURL;

    @Column(length = 11, columnDefinition = "INTEGER DEFAULT 0")
    private Integer clickNumber = 0;

    private Boolean enabled;

    private Boolean createdType;

    private Boolean validDateType;

    private LocalDateTime validDate;

    @Column(length = 1024)
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean archived;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime archivedAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
