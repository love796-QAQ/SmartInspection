package com.smart.biz.service;

import com.smart.biz.domain.BizRecord;
import java.util.List;

public interface IOfflineSyncService {
    /**
     * Sync offline records. Idempotent.
     * @param records List of records
     * @return List of successfully synced UUIDs (or IDs)
     */
    List<String> syncOfflineData(List<BizRecord> records);
}
