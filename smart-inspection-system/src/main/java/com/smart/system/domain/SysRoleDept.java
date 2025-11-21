package com.smart.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("sys_role_dept")
public class SysRoleDept implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long deptId;
}
