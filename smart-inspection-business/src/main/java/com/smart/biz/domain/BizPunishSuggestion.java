package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_punish_suggestion")
public class BizPunishSuggestion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long suggestionId;

    private Long userId;
    private Long deptId;
    
    /**
     * TASK_FAIL / MANUAL / LAB_FAIL
     */
    private String triggerSource;
    
    private Long triggerRefId;
    private Long suggestedLevelId;
    private BigDecimal suggestedAmount;
    private String reason;
    
    /**
     * 0:Pending, 1:Confirmed, 2:Rejected
     */
    private Integer status;
    
    private String managerComment;
    
    private String processInstanceId;
}
