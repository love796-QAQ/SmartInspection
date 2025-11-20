package com.smart.api.controller.admin;

import com.smart.common.vo.ChartDataVO;
import com.smart.biz.service.IDashboardService;
import com.smart.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("/issues/distribution")
    public Result<ChartDataVO> getIssueDistribution() {
        return Result.success(dashboardService.getIssueDistribution());
    }

    @GetMapping("/trend")
    public Result<ChartDataVO> getTrend() {
        return Result.success(dashboardService.getTrendAnalysis());
    }

    @GetMapping("/risk")
    public Result<ChartDataVO> getRiskProfile() {
        return Result.success(dashboardService.getDeptRiskProfile());
    }
}
