package com.smart.api.controller.admin;

import com.smart.common.core.Result;
import com.smart.system.service.IWorkflowService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/workflow")
public class WorkflowController {

    @Autowired
    private IWorkflowService workflowService;

    @PostMapping("/deploy")
    public Result<String> deploy(@RequestBody Map<String, String> params) {
        String resourceName = params.get("resourceName");
        String text = params.get("xml");
        return Result.success(workflowService.deployProcess(resourceName, text));
    }

    @PostMapping("/start")
    public Result<String> start(@RequestBody Map<String, Object> params) {
        String processDefinitionKey = (String) params.get("processDefinitionKey");
        String businessKey = (String) params.get("businessKey");
        Map<String, Object> variables = (Map<String, Object>) params.get("variables");
        ProcessInstance instance = workflowService.startProcess(processDefinitionKey, businessKey, variables);
        return Result.success(instance.getId());
    }

    @GetMapping("/tasks")
    public Result<List<Map<String, Object>>> getTasks(@RequestParam String assignee) {
        List<Task> tasks = workflowService.getTasks(assignee);
        List<Map<String, Object>> result = tasks.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("assignee", task.getAssignee());
            map.put("createTime", task.getCreateTime());
            map.put("processInstanceId", task.getProcessInstanceId());
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }

    @PostMapping("/complete")
    public Result<Void> complete(@RequestBody Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        Map<String, Object> variables = (Map<String, Object>) params.get("variables");
        workflowService.completeTask(taskId, variables);
        return Result.success(null);
    }
    
    @GetMapping("/history")
    public Result<List<Map<String, Object>>> getHistory(@RequestParam String assignee) {
        List<HistoricTaskInstance> tasks = workflowService.getHistoryTasks(assignee);
        List<Map<String, Object>> result = tasks.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("assignee", task.getAssignee());
            map.put("startTime", task.getStartTime());
            map.put("endTime", task.getEndTime());
            map.put("processInstanceId", task.getProcessInstanceId());
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }
}
