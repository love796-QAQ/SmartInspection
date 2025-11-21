package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_template")
public class BaseTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long templateId;

    private String templateName;
    private Long deptId;
    private Integer status;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private java.util.List<Long> itemIds;
}
