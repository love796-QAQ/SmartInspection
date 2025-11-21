package com.smart.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.biz.domain.BaseTemplate;
import com.smart.biz.service.IBaseTemplateService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/config/template")
public class InspectionTemplateController {

    @Autowired
    private IBaseTemplateService templateService;

    @GetMapping("/list")
    public Result<Page<BaseTemplate>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           BaseTemplate template) {
        return Result.success(templateService.page(new Page<>(pageNum, pageSize)));
    }

    @GetMapping("/{id}")
    public Result<BaseTemplate> getInfo(@PathVariable Long id) {
        BaseTemplate template = templateService.getById(id);
        template.setItemIds(templateService.selectTemplateItemIds(id));
        return Result.success(template);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody BaseTemplate template) {
        return Result.success(templateService.insertTemplate(template));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody BaseTemplate template) {
        return Result.success(templateService.updateTemplate(template));
    }

    @DeleteMapping("/{ids}")
    public Result<Boolean> remove(@PathVariable Long[] ids) {
        return Result.success(templateService.removeBatchByIds(java.util.Arrays.asList(ids)));
    }
}
