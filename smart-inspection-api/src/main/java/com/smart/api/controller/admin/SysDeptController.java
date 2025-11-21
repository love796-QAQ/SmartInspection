package com.smart.api.controller.admin;

import com.smart.common.core.Result;
import com.smart.system.domain.SysDept;
import com.smart.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;

    @GetMapping("/list")
    public Result<List<SysDept>> list(SysDept dept) {
        return Result.success(deptService.list());
    }

    @GetMapping("/{deptId}")
    public Result<SysDept> getInfo(@PathVariable Long deptId) {
        return Result.success(deptService.getById(deptId));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysDept dept) {
        return Result.success(deptService.save(dept));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysDept dept) {
        return Result.success(deptService.updateById(dept));
    }

    @DeleteMapping("/{deptId}")
    public Result<Boolean> remove(@PathVariable Long deptId) {
        return Result.success(deptService.removeById(deptId));
    }
}
