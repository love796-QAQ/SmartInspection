package com.smart.api.controller.admin;

import com.smart.biz.domain.BaseInspectionCategory;
import com.smart.biz.domain.BaseInspectionLevel;
import com.smart.biz.service.IBaseInspectionCategoryService;
import com.smart.biz.service.IBaseInspectionLevelService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/config/base")
public class InspectionBaseController {

    @Autowired
    private IBaseInspectionLevelService levelService;

    @Autowired
    private IBaseInspectionCategoryService categoryService;

    // === Level Management ===

    @GetMapping("/level/list")
    public Result<List<BaseInspectionLevel>> listLevel() {
        return Result.success(levelService.list());
    }

    @PostMapping("/level")
    public Result<Boolean> addLevel(@RequestBody BaseInspectionLevel level) {
        return Result.success(levelService.save(level));
    }

    @PutMapping("/level")
    public Result<Boolean> editLevel(@RequestBody BaseInspectionLevel level) {
        return Result.success(levelService.updateById(level));
    }

    @DeleteMapping("/level/{ids}")
    public Result<Boolean> removeLevel(@PathVariable Long[] ids) {
        return Result.success(levelService.removeBatchByIds(java.util.Arrays.asList(ids)));
    }

    // === Category Management ===

    @GetMapping("/category/list")
    public Result<List<BaseInspectionCategory>> listCategory() {
        return Result.success(categoryService.list());
    }

    @PostMapping("/category")
    public Result<Boolean> addCategory(@RequestBody BaseInspectionCategory category) {
        return Result.success(categoryService.save(category));
    }

    @PutMapping("/category")
    public Result<Boolean> editCategory(@RequestBody BaseInspectionCategory category) {
        return Result.success(categoryService.updateById(category));
    }

    @DeleteMapping("/category/{ids}")
    public Result<Boolean> removeCategory(@PathVariable Long[] ids) {
        return Result.success(categoryService.removeBatchByIds(java.util.Arrays.asList(ids)));
    }
}
