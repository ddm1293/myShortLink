package org.myShortLink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myShortLink.admin.dao.entity.UserDO;
import org.myShortLink.admin.dao.mapper.UserMapper;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Override
    public UserRespDTO getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) return null;
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(userDO, res);
        return res;
    }
}
