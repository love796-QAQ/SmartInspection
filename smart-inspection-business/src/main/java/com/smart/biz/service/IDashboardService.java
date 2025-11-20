package com.smart.biz.service;

import com.smart.common.vo.ChartDataVO;

public interface IDashboardService {
    /**
     * Get Issue Distribution by Level (Pie Chart)
     */
    ChartDataVO getIssueDistribution();

    /**
     * Get Trend Analysis (Line Chart)
     */
    ChartDataVO getTrendAnalysis();
    
    /**
     * Get Department Risk Profile (Radar Chart)
     */
    ChartDataVO getDeptRiskProfile();
}
