package com.smart.api.controller.admin;

import com.smart.biz.engine.PerformanceCalculator;
import com.smart.biz.service.IBizPerformanceResultService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/performance")
public class PerformanceController {

    @Autowired
    private PerformanceCalculator calculator;
    
    @Autowired
    private IBizPerformanceResultService resultService;

    @PostMapping("/calculate")
    public Result<Boolean> triggerCalculation(@RequestParam Long userId, @RequestParam Long deptId, @RequestParam String period) {
        calculator.calculate(userId, deptId, period);
        return Result.success(true);
    }
    
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(resultService.list());
    }
}
