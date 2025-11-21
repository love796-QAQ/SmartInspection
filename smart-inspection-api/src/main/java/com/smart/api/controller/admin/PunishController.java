package com.smart.api.controller.admin;

import com.smart.biz.domain.BizPunishSuggestion;
import com.smart.biz.service.IBizPunishSuggestionService;
import com.smart.common.core.Result;
import com.smart.system.service.IWorkflowService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/punish")
public class PunishController {

    @Autowired
    private IBizPunishSuggestionService punishService;

    @Autowired
    private IWorkflowService workflowService;

    @PostMapping("/submit")
    public Result<Long> submit(@RequestBody BizPunishSuggestion suggestion) {
        // 1. Save business data
        suggestion.setStatus(0); // Pending
        punishService.save(suggestion);

        // 2. Start workflow
        Map<String, Object> variables = new HashMap<>();
        variables.put("amount", suggestion.getSuggestedAmount());
        variables.put("initiator", suggestion.getUserId());
        
        ProcessInstance instance = workflowService.startProcess("punish_process", suggestion.getSuggestionId().toString(), variables);
        
        // 3. Update business data with process instance ID
        suggestion.setProcessInstanceId(instance.getId());
        punishService.updateById(suggestion);

        return Result.success(suggestion.getSuggestionId());
    }

    @PostMapping("/approve")
    public Result<Void> approve(@RequestBody Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        Long suggestionId = Long.valueOf((String) params.get("businessKey"));
        Boolean approved = (Boolean) params.get("approved");
        String comment = (String) params.get("comment");

        // 1. Complete workflow task
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        workflowService.completeTask(taskId, variables);

        // 2. Update business status if rejected or final approval
        // Note: In a real scenario, we should listen to process end events.
        // Here for simplicity, if rejected, we update status immediately.
        if (!approved) {
            BizPunishSuggestion suggestion = new BizPunishSuggestion();
            suggestion.setSuggestionId(suggestionId);
            suggestion.setStatus(2); // Rejected
            suggestion.setManagerComment(comment);
            punishService.updateById(suggestion);
        }

        return Result.success(null);
    }
    
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(punishService.list());
    }
}
