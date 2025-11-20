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
    
    private String description;
}
