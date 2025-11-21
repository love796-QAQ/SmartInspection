package com.smart.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.system.domain.SysRole;
import java.util.List;

public interface ISysRoleService extends IService<SysRole> {
    boolean insertRole(SysRole role);
    boolean updateRole(SysRole role);
    List<Long> selectRoleDeptIds(Long roleId);
}
