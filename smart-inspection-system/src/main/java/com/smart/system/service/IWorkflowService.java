package com.smart.system.service;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.List;
import java.util.Map;

public interface IWorkflowService {

    /**
     * Deploy process definition
     * @param resourceName Resource name (e.g., "process.bpmn20.xml")
     * @param text BPMN XML content
     * @return Deployment ID
     */
    String deployProcess(String resourceName, String text);

    /**
     * Start a process instance
     * @param processDefinitionKey Process definition key
     * @param businessKey Business key (e.g., Order ID)
     * @param variables Process variables
     * @return ProcessInstance
     */
    ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * Get tasks assigned to a user
     * @param assignee User ID
     * @return List of tasks
     */
    List<Task> getTasks(String assignee);

    /**
     * Complete a task
     * @param taskId Task ID
     * @param variables Process variables
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * Get historic tasks for a user
     * @param assignee User ID
     * @return List of historic tasks
     */
    List<HistoricTaskInstance> getHistoryTasks(String assignee);
}
