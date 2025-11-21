package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.system.domain.SysRole;
import com.smart.system.domain.SysRoleDept;
import com.smart.system.mapper.SysRoleMapper;
import com.smart.system.service.ISysRoleDeptService;
import com.smart.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleDeptService roleDeptService;

    @Override
    @Transactional
    public boolean insertRole(SysRole role) {
        boolean result = save(role);
        insertRoleDept(role);
        return result;
    }

    @Override
    @Transactional
    public boolean updateRole(SysRole role) {
        boolean result = updateById(role);
        // Delete existing role-dept relations
        roleDeptService.remove(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, role.getRoleId()));
        insertRoleDept(role);
        return result;
    }

    @Override
    public List<Long> selectRoleDeptIds(Long roleId) {
        return roleDeptService.list(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, roleId))
                .stream().map(SysRoleDept::getDeptId).collect(Collectors.toList());
    }

    private void insertRoleDept(SysRole role) {
        if ("5".equals(role.getDataScope()) && role.getDeptIds() != null && !role.getDeptIds().isEmpty()) {
            List<SysRoleDept> list = new ArrayList<>();
            for (Long deptId : role.getDeptIds()) {
                SysRoleDept rd = new SysRoleDept();
                rd.setRoleId(role.getRoleId());
                rd.setDeptId(deptId);
                list.add(rd);
            }
            if (!list.isEmpty()) {
                roleDeptService.saveBatch(list);
            }
        }
    }
}
