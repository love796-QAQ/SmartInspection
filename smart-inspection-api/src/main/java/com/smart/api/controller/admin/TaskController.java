import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.biz.domain.BizTask;
import com.smart.biz.service.IBizTaskService;
import com.smart.common.core.Result;
import com.smart.system.service.ISysDeptService;
import com.smart.system.service.ISysUserService;
import com.smart.biz.service.IBaseTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/task")
public class TaskController {

    @Autowired
    private IBizTaskService taskService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IBaseTemplateService templateService;

    @PostMapping("/assign")
    public Result<Long> assignTask(@RequestBody BizTask task) {
        // Manual assignment
        task.setStatus(0);
        Long taskId = taskService.createTask(task);
        return Result.success(taskId);
    }
    
    @GetMapping("/list")
    @com.smart.common.annotation.DataScope(deptAlias = "d", userAlias = "u")
    public Result<Page<BizTask>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      BizTask task) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BizTask> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>(task);
        
        // Handle Data Scope
        String dataScope = (String) task.getParams().get("dataScope");
        if (dataScope != null && !dataScope.isEmpty()) {
            wrapper.apply(dataScope);
        }
        
        Page<BizTask> page = taskService.page(new Page<>(pageNum, pageSize), wrapper);
        page.getRecords().forEach(item -> {
            if (item.getInspectorId() != null) {
                try {
                    item.setInspectorName(userService.getById(item.getInspectorId()).getRealName());
                } catch (Exception e) {}
            }
            if (item.getDeptId() != null) {
                try {
                    item.setDeptName(deptService.getById(item.getDeptId()).getDeptName());
                } catch (Exception e) {}
            }
            if (item.getTemplateId() != null) {
                try {
                    item.setTemplateName(templateService.getById(item.getTemplateId()).getTemplateName());
                } catch (Exception e) {}
            }
        });
        return Result.success(page);
    }

    @GetMapping("/{taskId}")
    public Result<BizTask> getInfo(@PathVariable Long taskId) {
        BizTask task = taskService.getById(taskId);
        // Populate names
        if (task.getInspectorId() != null) {
            try {
                task.setInspectorName(userService.getById(task.getInspectorId()).getRealName());
            } catch (Exception e) {}
        }
        if (task.getDeptId() != null) {
            try {
                task.setDeptName(deptService.getById(task.getDeptId()).getDeptName());
            } catch (Exception e) {}
        }
        if (task.getTemplateId() != null) {
            try {
                task.setTemplateName(templateService.getById(task.getTemplateId()).getTemplateName());
            } catch (Exception e) {}
        }
        return Result.success(task);
    }

    @Autowired
    private com.smart.biz.service.IBizTaskItemService taskItemService;

    @GetMapping("/{taskId}/items")
    public Result<List<com.smart.biz.domain.BizTaskItem>> getTaskItems(@PathVariable Long taskId) {
        return Result.success(taskItemService.list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.smart.biz.domain.BizTaskItem>()
                .eq(com.smart.biz.domain.BizTaskItem::getTaskId, taskId)));
    }
}
