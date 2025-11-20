package com.smart.api.controller.admin;

import com.smart.common.core.Result;
import com.smart.system.domain.SysUser;
import com.smart.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @GetMapping("/{id}")
    public Result<SysUser> getInfo(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysUser user) {
        return Result.success(userService.save(user));
    }
}
