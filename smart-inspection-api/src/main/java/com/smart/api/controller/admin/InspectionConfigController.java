package com.smart.api.controller.admin;

import com.smart.biz.domain.BaseInspectionItem;
import com.smart.biz.service.IBaseInspectionItemService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/config/item")
public class InspectionConfigController {

    @Autowired
    private IBaseInspectionItemService itemService;

    @GetMapping("/list")
    public Result<List<BaseInspectionItem>> list() {
        return Result.success(itemService.list());
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody BaseInspectionItem item) {
        return Result.success(itemService.save(item));
    }
}
