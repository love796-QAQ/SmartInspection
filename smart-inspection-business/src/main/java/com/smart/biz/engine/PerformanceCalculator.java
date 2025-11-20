package com.smart.biz.engine;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.biz.domain.BaseInspectionLevel;
import com.smart.biz.domain.BizPerformanceResult;
import com.smart.biz.service.IBaseInspectionLevelService;
import com.smart.biz.service.IBizPerformanceResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PerformanceCalculator {

    @Autowired
    private IBizPerformanceResultService performanceService;
    
    @Autowired
    private IBaseInspectionLevelService levelService;

    /**
     * Calculate performance for a user in a period
     * @param userId Target User
     * @param deptId Department
     * @param period Period string (e.g. "2023-10")
     */
    public void calculate(Long userId, Long deptId, String period) {
        log.info("Calculating performance for user {} in period {}", userId, period);
        
        // 1. Check if result already exists (and is confirmed)
        long count = performanceService.count(new LambdaQueryWrapper<BizPerformanceResult>()
                .eq(BizPerformanceResult::getUserId, userId)
                .eq(BizPerformanceResult::getPeriod, period)
                .eq(BizPerformanceResult::getStatus, 1)); // Confirmed
        if (count > 0) {
            log.info("Performance already confirmed for user {} in period {}", userId, period);
            return;
        }

        // 2. Fetch Levels and Weights
        List<BaseInspectionLevel> levels = levelService.list();
        Map<Long, Integer> levelWeights = levels.stream()
                .collect(Collectors.toMap(BaseInspectionLevel::getLevelId, BaseInspectionLevel::getScoreWeight));

        // 3. Fetch Inspection Data (Mocking aggregation for now)
        // In reality: Query BizRecordDetail joined with BizRecord filtered by date range and user
        int totalInspections = 10; // Mock
        int l1Count = 1; // Mock
        int l2Count = 2; // Mock
        int l3Count = 0; // Mock
        
        // 4. Calculate Deductions
        BigDecimal deduction = BigDecimal.ZERO;
        // Assuming Level IDs 1, 2, 3 correspond to L1, L2, L3 (Need better lookup in real app)
        deduction = deduction.add(BigDecimal.valueOf(l1Count * levelWeights.getOrDefault(1L, 10)));
        deduction = deduction.add(BigDecimal.valueOf(l2Count * levelWeights.getOrDefault(2L, 5)));
        deduction = deduction.add(BigDecimal.valueOf(l3Count * levelWeights.getOrDefault(3L, 2)));

        // 5. Save Result
        BizPerformanceResult result = new BizPerformanceResult();
        result.setUserId(userId);
        result.setDeptId(deptId);
        result.setPeriod(period);
        result.setTotalInspections(totalInspections);
        result.setTotalIssues(l1Count + l2Count + l3Count);
        result.setL1Issues(l1Count);
        result.setL2Issues(l2Count);
        result.setL3Issues(l3Count);
        result.setOriginalScore(new BigDecimal("100"));
        result.setDeductionScore(deduction);
        result.setFinalScore(new BigDecimal("100").subtract(deduction));
        result.setStatus(0); // Pending Confirmation
        
        // Update existing pending or create new
        BizPerformanceResult existing = performanceService.getOne(new LambdaQueryWrapper<BizPerformanceResult>()
                .eq(BizPerformanceResult::getUserId, userId)
                .eq(BizPerformanceResult::getPeriod, period)
                .eq(BizPerformanceResult::getStatus, 0));
        
        if (existing != null) {
            result.setResultId(existing.getResultId());
            performanceService.updateById(result);
        } else {
            performanceService.save(result);
        }
        
        log.info("Calculated score: {}", result.getFinalScore());
    }
}
