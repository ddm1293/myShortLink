package org.myShortLink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.myShortLink.admin.dao.entity.UserDO;
import org.myShortLink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

        UserRespDTO getUserByUserName(String username);
}
