package org.myShortLink.project.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * helper class, serving as a router
 */

@Data
@Entity
@Table(name = "t_link_router")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkRouter {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator")
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.common.config.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(length = 56)
    private String gid;

    @Column(unique = true, columnDefinition = "VARCHAR(128) COLLATE utf8mb4_bin")
    private String fullShortUrl;
}

