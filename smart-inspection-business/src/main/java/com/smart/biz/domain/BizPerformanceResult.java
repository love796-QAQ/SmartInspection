package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_performance_result")
public class BizPerformanceResult extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long resultId;

    private Long userId;
    private Long deptId;
    private String period; // e.g., "2023-10" or "2023-W42"
    
    private Integer totalInspections;
    private Integer totalIssues;
    
    // Breakdown by level (stored as JSON or separate columns, simplified here)
    private Integer l1Issues;
    private Integer l2Issues;
    private Integer l3Issues;
    
    private BigDecimal originalScore; // e.g. 100
    private BigDecimal deductionScore;
    private BigDecimal finalScore;
    
    /**
     * 0:Pending, 1:Confirmed
     */
    private Integer status;
    
    private Long confirmUserId;
}
