package com.smart.api.controller.admin;

import com.smart.biz.domain.BizTask;
import com.smart.biz.service.IBizTaskService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/task")
public class TaskController {

    @Autowired
    private IBizTaskService taskService;

    @PostMapping("/assign")
    public Result<Long> assignTask(@RequestBody BizTask task) {
        // Manual assignment
        task.setStatus(0);
        Long taskId = taskService.createTask(task);
        return Result.success(taskId);
    }
    
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(taskService.list());
    }
}
