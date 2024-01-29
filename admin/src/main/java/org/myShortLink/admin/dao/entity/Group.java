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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="t_group", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"gid", "username"})
})
public class Group {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator"
    )
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.common.config.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(length = 56, nullable = false)
    private String gid;

    @Column(length = 56)
    private String groupName;

    @Column(length = 256)
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
