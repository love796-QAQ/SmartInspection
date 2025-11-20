package com.smart.biz.service.impl;

import com.smart.biz.domain.BizRecord;
import com.smart.biz.service.IBizRecordService;
import com.smart.biz.service.IOfflineSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OfflineSyncServiceImpl implements IOfflineSyncService {

    @Autowired
    private IBizRecordService recordService;
    
    @Autowired
    private com.smart.biz.service.IBizRecordDetailService detailService;
    
    @Autowired
    private com.smart.biz.service.IBizIssueService issueService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> syncOfflineData(List<BizRecord> records) {
        List<String> syncedIds = new ArrayList<>();

        for (BizRecord record : records) {
            try {
                // 1. Save Record
                record.setIsOfflineSubmission(1);
                recordService.save(record);
                
                // 2. Save Details & Check for Issues
                if (record.getDetails() != null) {
                    for (com.smart.biz.domain.BizRecordDetail detail : record.getDetails()) {
                        detail.setRecordId(record.getRecordId());
                        detailService.save(detail);
                        
                        // 3. Create Issue if Failed
                        if (detail.getIsPass() != null && detail.getIsPass() == 0) {
                            com.smart.biz.domain.BizIssue issue = new com.smart.biz.domain.BizIssue();
                            issue.setRecordDetailId(detail.getDetailId());
                            // Assuming we can get deptId from task or record (simplified here)
                            issue.setDeptId(100L); // Placeholder
                            issue.setStatus(0); // Pending Rectification
                            issue.setRectifyDeadline(java.time.LocalDateTime.now().plusDays(3)); // Default 3 days
                            issueService.save(issue);
                        }
                    }
                }
                
                syncedIds.add(String.valueOf(record.getRecordId()));
            } catch (Exception e) {
                log.error("Failed to sync record", e);
            }
        }
        return syncedIds;
    }
}
