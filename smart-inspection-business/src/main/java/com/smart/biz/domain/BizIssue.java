package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_issue")
public class BizIssue extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long issueId;

    private Long recordDetailId;
    private Long deptId;
    private Long responsibleUserId;
    
    /**
     * 0:Pending Rectification, 1:Rectified(Pending Review), 2:Closed, 3:Rejected
     */
    private Integer status;
    
    private LocalDateTime rectifyDeadline;
    private String rectifyImage;
    private String rectifyDesc;
    
    private Long verifyUserId;
    private LocalDateTime verifyTime;
}
