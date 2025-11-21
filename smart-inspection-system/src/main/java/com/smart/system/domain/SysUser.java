package com.smart.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long userId;

    private String username;
    private String password;
    private String realName;
    private Long deptId;
    private Long roleId;
    
    /**
     * 1:Enable, 0:Disable
     */
    /**
     * 1:Enable, 0:Disable
     */
    private Integer status;

    public boolean isAdmin() {
        return userId != null && userId == 1L;
    }
}
