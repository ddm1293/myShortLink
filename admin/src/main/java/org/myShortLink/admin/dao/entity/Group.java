package org.myShortLink.admin.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.myShortLink.common.database.BaseDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="t_group", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"gid", "username"})
})
public class Group extends BaseDO {

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

    @Column(length = 256, updatable = false)
    private String username;

    @Builder.Default
    @Column(length = 3, columnDefinition = "INTEGER DEFAULT 0")
    private Integer sortOrder = 0;
}
