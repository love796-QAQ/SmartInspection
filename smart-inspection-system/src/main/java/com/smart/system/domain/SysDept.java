package com.smart.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long deptId;

    private String deptName;
    private Long parentId;
    private String ancestors;
    
    /**
     * 1:School, 2:Canteen, 3:Area
     */
    private Integer deptType;
}
