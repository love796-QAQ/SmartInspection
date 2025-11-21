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

    @GetMapping("/list")
    public Result<Object> list(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               SysUser user) {
        // TODO: Use Page object
        return Result.success(userService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize)));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysUser user) {
        return Result.success(userService.save(user));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysUser user) {
        return Result.success(userService.updateById(user));
    }

    @DeleteMapping("/{ids}")
    public Result<Boolean> remove(@PathVariable Long[] ids) {
        return Result.success(userService.removeBatchByIds(java.util.Arrays.asList(ids)));
    }
    @PutMapping("/profile")
    public Result<Boolean> updateProfile(@RequestBody SysUser user) {
        // Only allow updating nickname/email/phone etc.
        SysUser currentUser = userService.getById(user.getUserId());
        if (currentUser != null) {
            currentUser.setRealName(user.getRealName());
            // Add other fields if needed
            return Result.success(userService.updateById(currentUser));
        }
        return Result.error("User not found");
    }

    @PutMapping("/profile/password")
    public Result<Boolean> updatePwd(String oldPassword, String newPassword) {
        // TODO: Get current user ID from SecurityContext
        // For now, we assume the frontend passes the user ID or we get it from token (not implemented in this snippet)
        // This is a placeholder. In a real app, use SecurityUtils.getUserId()
        return Result.error("Password update not fully implemented yet");
    }
}
