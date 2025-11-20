package com.smart.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.domain.SysUser;

public interface ISysUserService extends IService<SysUser> {
    /**
     * Get user by username
     * @param username username
     * @return SysUser
     */
    SysUser selectUserByUserName(String username);
}
