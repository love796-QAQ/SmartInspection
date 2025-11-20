package com.smart.biz.engine;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.biz.domain.BaseInspectionLevel;
import com.smart.biz.domain.BizPunishSuggestion;
import com.smart.biz.domain.BizRecordDetail;
import com.smart.biz.service.IBaseInspectionLevelService;
import com.smart.biz.service.IBizPunishSuggestionService;
import com.smart.biz.service.IBizRecordDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PunishmentRuleEngine {

    @Autowired
    private IBaseInspectionLevelService levelService;
    
    @Autowired
    private IBizRecordDetailService recordDetailService;
    
    @Autowired
    private IBizPunishSuggestionService suggestionService;

    /**
     * Evaluate punishment rules for a specific user
     * @param userId Target User ID
     * @param deptId Department ID
     */
    public void evaluate(Long userId, Long deptId) {
        log.info("Starting punishment evaluation for user: {}", userId);
        
        // 1. Get all enabled levels with punishment rules
        List<BaseInspectionLevel> levels = levelService.list(new LambdaQueryWrapper<BaseInspectionLevel>()
                .eq(BaseInspectionLevel::getIsEnabled, 1)
                .isNotNull(BaseInspectionLevel::getPunishRuleJson));

        // 2. Get recent records (e.g., last 30 days)
        Date startDate = DateUtil.offsetDay(new Date(), -30);
        
        // Note: In a real scenario, we would need to join BizRecord to filter by userId.
        // For simplicity here, assuming we have a service method to get details by user.
        // We will implement a custom query in Mapper later. 
        // Here we simulate fetching all details for the user (needs optimization).
        
        // 3. Iterate levels and check rules
        for (BaseInspectionLevel level : levels) {
            String ruleJson = level.getPunishRuleJson();
            if (StrUtil.isBlank(ruleJson)) continue;

            JSONObject rule = JSONUtil.parseObj(ruleJson);
            Integer consecutiveLimit = rule.getInt("consecutive");
            Integer cumulativeLimit = rule.getInt("cumulative");
            BigDecimal amount = rule.getBigDecimal("amount");

            if (amount == null) continue;

            // Logic: Check if user violated this level > limit
            // This requires complex querying.
            // For MVP, let's implement a simple "Cumulative Count" check.
            
            long count = countViolations(userId, level.getLevelId(), startDate);
            
            if (cumulativeLimit != null && count >= cumulativeLimit) {
                createSuggestion(userId, deptId, level, amount, "Cumulative violations: " + count);
            }
        }
    }

    private long countViolations(Long userId, Long levelId, Date startDate) {
        // Placeholder: In reality, need to join BizRecord to filter by inspector/responsible person
        // This is a simplified logic assuming we can query details directly.
        // We need to add 'responsibleUserId' to BizRecordDetail or link via BizRecord.
        return 0; // TODO: Implement actual count query
    }

    private void createSuggestion(Long userId, Long deptId, BaseInspectionLevel level, BigDecimal amount, String reason) {
        // Check if already suggested recently to avoid spam
        Long count = suggestionService.count(new LambdaQueryWrapper<BizPunishSuggestion>()
                .eq(BizPunishSuggestion::getUserId, userId)
                .eq(BizPunishSuggestion::getSuggestedLevelId, level.getLevelId())
                .eq(BizPunishSuggestion::getStatus, 0)); // Pending
        
        if (count > 0) return;

        BizPunishSuggestion suggestion = new BizPunishSuggestion();
        suggestion.setUserId(userId);
        suggestion.setDeptId(deptId);
        suggestion.setTriggerSource("AUTO_RULE");
        suggestion.setSuggestedLevelId(level.getLevelId());
        suggestion.setSuggestedAmount(amount);
        suggestion.setReason(reason);
        suggestion.setStatus(0); // Pending
        
        suggestionService.save(suggestion);
        log.info("Created punishment suggestion for user {}", userId);
    }
}
