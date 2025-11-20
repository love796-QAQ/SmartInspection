package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_task")
public class BizTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long taskId;

    private String taskName;
    private Long templateId;
    private Long deptId;
    private Long inspectorId;
    
    /**
     * 0:Pending, 1:In Progress, 2:Completed, 3:Expired
     */
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
}
