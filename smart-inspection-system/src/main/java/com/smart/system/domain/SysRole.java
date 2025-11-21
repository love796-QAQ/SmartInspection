package com.smart.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long roleId;

    private String roleName;
    
    /**
     * LEVEL1...LEVEL6
     */
    private String roleCode;
    
    /**
     * 1:Admin...6:AreaHead
     */
    private Integer level;

    /**
     * 1:All 2:Dept+Sub 3:Dept 4:Self 5:Custom
     */
    private String dataScope;
    
    private String description;
    
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private java.util.List<Long> deptIds;
}
