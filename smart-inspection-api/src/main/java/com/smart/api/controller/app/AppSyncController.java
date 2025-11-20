package com.smart.api.controller.app;

import com.smart.biz.domain.BizRecord;
import com.smart.biz.service.IOfflineSyncService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/sync")
public class AppSyncController {

    @Autowired
    private IOfflineSyncService syncService;

    @PostMapping("/submit")
    public Result<List<String>> submit(@RequestBody List<BizRecord> records) {
        return Result.success(syncService.syncOfflineData(records));
    }
}
