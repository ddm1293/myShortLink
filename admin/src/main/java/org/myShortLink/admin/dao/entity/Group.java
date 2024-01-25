package org.myShortLink.admin.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="t_group")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator"
    )
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.admin.config.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(length = 56, nullable = false, unique = true)
    private String gid;

    @Column(length = 56)
    private String groupName;

    @Column(length = 256, unique = true)
    private String username;

    @Builder.Default
    @Column(length = 3, columnDefinition = "INTEGER DEFAULT 0")
    private Integer sortOrder = 0;

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
