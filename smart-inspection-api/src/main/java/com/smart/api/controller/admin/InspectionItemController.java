package com.smart.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.biz.domain.BaseInspectionItem;
import com.smart.biz.service.IBaseInspectionItemService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/config/item")
public class InspectionItemController {

    @Autowired
    private IBaseInspectionItemService itemService;

    @GetMapping("/list")
    public Result<Page<BaseInspectionItem>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 BaseInspectionItem item) {
        return Result.success(itemService.page(new Page<>(pageNum, pageSize)));
    }

    @GetMapping("/{id}")
    public Result<BaseInspectionItem> getInfo(@PathVariable Long id) {
        return Result.success(itemService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody BaseInspectionItem item) {
        return Result.success(itemService.save(item));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody BaseInspectionItem item) {
        return Result.success(itemService.updateById(item));
    }

    @DeleteMapping("/{ids}")
    public Result<Boolean> remove(@PathVariable Long[] ids) {
        return Result.success(itemService.removeBatchByIds(java.util.Arrays.asList(ids)));
    }
}
