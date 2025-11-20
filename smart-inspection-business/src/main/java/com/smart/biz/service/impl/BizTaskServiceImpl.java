package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizTask;
import com.smart.biz.mapper.BizTaskMapper;
import com.smart.biz.service.IBizTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BizTaskServiceImpl extends ServiceImpl<BizTaskMapper, BizTask> implements IBizTaskService {

    @Autowired
    private com.smart.biz.service.IBaseTemplateItemService templateItemService;
    
    @Autowired
    private com.smart.biz.service.IBaseInspectionItemService inspectionItemService;
    
    @Autowired
    private com.smart.biz.service.IBizTaskItemService taskItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(BizTask task) {
        // 1. Save Task
        this.save(task);
        
        // 2. Create Snapshot
        if (task.getTemplateId() != null) {
            // Fetch template items
            var templateItems = templateItemService.list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.smart.biz.domain.BaseTemplateItem>()
                    .eq(com.smart.biz.domain.BaseTemplateItem::getTemplateId, task.getTemplateId()));
            
            for (var ti : templateItems) {
                var originalItem = inspectionItemService.getById(ti.getItemId());
                if (originalItem != null) {
                    com.smart.biz.domain.BizTaskItem snapshot = new com.smart.biz.domain.BizTaskItem();
                    snapshot.setTaskId(task.getTaskId());
                    snapshot.setOriginalItemId(originalItem.getItemId());
                    snapshot.setItemName(originalItem.getItemName());
                    snapshot.setCategoryId(originalItem.getCategoryId());
                    snapshot.setLevelId(originalItem.getDefaultLevelId());
                    snapshot.setStandardDesc(originalItem.getStandardDesc());
                    snapshot.setSortOrder(ti.getSortOrder());
                    
                    taskItemService.save(snapshot);
                }
            }
        }
        
        return task.getTaskId();
    }
}
