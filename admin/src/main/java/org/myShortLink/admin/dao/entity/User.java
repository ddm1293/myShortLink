package org.myShortLink.admin.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.myShortLink.common.database.BaseDO;

@Entity
@Table(name = "t_user")
@Data
public class User extends BaseDO {

    @Id
    @GeneratedValue(
            generator = "snowflakeIdGenerator")
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.myShortLink.common.config.SnowflakeIdGenerator"
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
}
