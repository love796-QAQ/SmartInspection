package com.smart.biz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.biz.domain.BizIssue;
import com.smart.biz.domain.BizTask;
import com.smart.biz.service.IBizIssueService;
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
public class OverdueCheckJob extends QuartzJobBean {

    @Autowired
    private IBizTaskService taskService;
    
    @Autowired
    private IBizIssueService issueService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting Overdue Check Job...");
        
        checkOverdueTasks();
        checkOverdueRectifications();
        
        log.info("Overdue Check Job Completed.");
    }

    private void checkOverdueTasks() {
        // Find tasks that are NOT completed (status != 2) and deadline < now
        List<BizTask> overdueTasks = taskService.list(new LambdaQueryWrapper<BizTask>()
                .ne(BizTask::getStatus, 2) 
                .lt(BizTask::getDeadline, LocalDateTime.now()));
        
        for (BizTask task : overdueTasks) {
            log.warn("Task Overdue: ID={}, Name={}, Deadline={}", task.getTaskId(), task.getTaskName(), task.getDeadline());
            // TODO: Send Notification to Inspector/Manager
        }
    }

    private void checkOverdueRectifications() {
        // Find issues that are Pending Rectification (status=0) and deadline < now
        List<BizIssue> overdueIssues = issueService.list(new LambdaQueryWrapper<BizIssue>()
                .eq(BizIssue::getStatus, 0)
                .lt(BizIssue::getRectifyDeadline, LocalDateTime.now()));
        
        for (BizIssue issue : overdueIssues) {
            log.warn("Rectification Overdue: IssueID={}, Deadline={}", issue.getIssueId(), issue.getRectifyDeadline());
            // TODO: Send Notification to Responsible User
        }
    }
}
