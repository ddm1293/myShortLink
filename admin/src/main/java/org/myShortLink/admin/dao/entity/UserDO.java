package org.myShortLink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("t_user")
@Data
public class UserDO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Date createdAt;
    private int archived;
    private Date archivedAt;
    private Date updatedAt;
}
