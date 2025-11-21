package com.smart.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.core.Result;
import com.smart.system.domain.SysRole;
import com.smart.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @GetMapping("/list")
    public Result<Page<SysRole>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      SysRole role) {
        return Result.success(roleService.page(new Page<>(pageNum, pageSize)));
    }

    @GetMapping("/{roleId}")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        SysRole role = roleService.getById(roleId);
        role.setDeptIds(roleService.selectRoleDeptIds(roleId));
        return Result.success(role);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysRole role) {
        return Result.success(roleService.insertRole(role));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysRole role) {
        return Result.success(roleService.updateRole(role));
    }

    @DeleteMapping("/{roleIds}")
    public Result<Boolean> remove(@PathVariable Long[] roleIds) {
        return Result.success(roleService.removeBatchByIds(java.util.Arrays.asList(roleIds)));
    }
}
