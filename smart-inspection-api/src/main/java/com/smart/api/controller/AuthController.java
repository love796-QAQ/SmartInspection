package com.smart.api.controller;

import com.smart.api.dto.LoginBody;
import com.smart.common.core.Result;
import com.smart.common.utils.JwtUtils;
import com.smart.system.domain.SysUser;
import com.smart.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ISysUserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 1. Check user
        SysUser user = userService.selectUserByUserName(loginBody.getUsername());
        if (user == null) {
            return Result.error("User not found");
        }

        // 2. Check password (Plain text for now as per init.sql)
        if (!user.getPassword().equals(loginBody.getPassword())) {
            return Result.error("Invalid password");
        }

        // 3. Generate Token
        String token = JwtUtils.createToken(user.getUsername(), user.getUserId());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success(data);
    }
}
