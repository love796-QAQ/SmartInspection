package com.smart.biz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.biz.domain.BaseTemplate;
import com.smart.biz.domain.BizTask;
import com.smart.biz.service.IBaseTemplateService;
import com.smart.biz.service.IBizTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class TaskGenerationJob extends QuartzJobBean {

    @Autowired
    private IBaseTemplateService templateService;

    @Autowired
    private IBizTaskService taskService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting Task Generation Job...");

        // 1. Find active templates that need task generation
        // In a real system, we would check cron expressions or schedules linked to templates.
        // For MVP, we'll generate tasks for ALL active templates for the current week.
        
        List<BaseTemplate> templates = templateService.list(new LambdaQueryWrapper<BaseTemplate>()
                .eq(BaseTemplate::getStatus, 1));

        for (BaseTemplate template : templates) {
            generateTaskForTemplate(template);
        }
        
        log.info("Task Generation Job Completed.");
    }

    private void generateTaskForTemplate(BaseTemplate template) {
        // Check if task already exists for this period (e.g., this week)
        // Simplified: Just create a new one
        
        BizTask task = new BizTask();
        task.setTaskName(template.getTemplateName() + " - " + LocalDateTime.now().toLocalDate());
        task.setTemplateId(template.getTemplateId());
        task.setDeptId(template.getDeptId());
        task.setStatus(0); // Pending
        task.setStartTime(LocalDateTime.now());
        task.setDeadline(LocalDateTime.now().plusDays(7)); // 1 week deadline
        
        // Logic to assign inspector (Round robin or fixed)
        // task.setInspectorId(...); 
        
        taskService.createTask(task); // This handles snapshot creation
        log.info("Generated task: {}", task.getTaskName());
    }
}
