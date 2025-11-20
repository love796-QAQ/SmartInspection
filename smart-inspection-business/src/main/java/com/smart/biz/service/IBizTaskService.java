package com.smart.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.biz.domain.BizTask;

public interface IBizTaskService extends IService<BizTask> {
    /**
     * Create a new task with snapshot items
     * @param task Task info
     * @return Created Task ID
     */
    Long createTask(BizTask task);
}
