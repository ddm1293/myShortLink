package org.myShortLink.project.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.myShortLink.common.database.BaseDO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_link")
public class Link extends BaseDO {

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

    @Column(columnDefinition = "VARCHAR(8) COLLATE utf8mb4_bin")
    private String shortUrl;

    @Column(unique = true, columnDefinition = "VARCHAR(128) COLLATE utf8mb4_bin")
    private String fullShortUrl;

    @Column(length = 1024)
    private String originalUrl;

    @Column(length = 11, columnDefinition = "INTEGER DEFAULT 0")
    private Integer clickNumber = 0;

    private Boolean enabled;

    /**
     * Create Type:
     *      0: Created by interface
     *      1: Created by console
     */
    private Boolean createdType;

    /**
     * Valid Date Type:
     *      0: Permanent
     *      1: Custom
     */
    private Boolean validDateType;

    private LocalDateTime validDate;

    @Column(length = 1024)
    private String description;
}
