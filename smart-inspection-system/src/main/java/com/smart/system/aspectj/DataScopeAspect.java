package com.smart.system.aspectj;

import com.smart.common.annotation.DataScope;
import com.smart.common.core.BaseEntity;
import com.smart.system.domain.SysRole;
import com.smart.system.domain.SysUser;
import com.smart.system.service.ISysRoleService;
import com.smart.system.service.ISysUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
public class DataScopeAspect {

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysRoleService roleService;

    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 1. Get current user
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SysUser)) {
            return;
        }
        SysUser currentUser = (SysUser) authentication.getPrincipal();
        if (currentUser.isAdmin()) {
            return; // Super admin has full access
        }

        // 2. Get Role
        SysRole role = roleService.getById(currentUser.getRoleId());
        if (role == null) {
            return;
        }

        String dataScope = role.getDataScope();
        String deptAlias = controllerDataScope.deptAlias();
        String userAlias = controllerDataScope.userAlias();
        
        StringBuilder sqlString = new StringBuilder();

        if (DATA_SCOPE_ALL.equals(dataScope)) {
            return; // All data
        } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
            sqlString.append(String.format(
                " OR %s.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = %d ) ", 
                deptAlias, role.getRoleId()));
        } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
            sqlString.append(String.format(" OR %s.dept_id = %d ", deptAlias, currentUser.getDeptId()));
        } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
            sqlString.append(String.format(
                " OR %s.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = %d OR find_in_set( %d , ancestors ) )",
                deptAlias, currentUser.getDeptId(), currentUser.getDeptId()));
        } else if (DATA_SCOPE_SELF.equals(dataScope)) {
            if (!"".equals(userAlias)) {
                sqlString.append(String.format(" OR %s.user_id = %d ", userAlias, currentUser.getUserId()));
            } else {
                // Fallback: if no user alias, maybe filter by inspector_id or similar if applicable, 
                // but usually self scope implies filtering by creator or assignee.
                // For Task, it's inspector_id. For others, maybe create_by.
                // Let's assume the query uses 'inspector_id' if userAlias is 'inspector_id' or similar.
                // If userAlias is empty, we can't filter by user, so we default to no data or specific logic.
                sqlString.append(" OR 1=0 "); // No access if alias not provided
            }
        }

        if (sqlString.length() > 0) {
            Object params = joinPoint.getArgs()[0];
            if (params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put("dataScope", " AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (params != null && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put("dataScope", "");
        }
    }
}
