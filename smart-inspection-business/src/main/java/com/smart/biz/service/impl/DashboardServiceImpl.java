package com.smart.biz.service.impl;

import com.smart.api.vo.ChartDataVO;
import com.smart.biz.service.IDashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardServiceImpl implements IDashboardService {

    @Override
    public ChartDataVO getIssueDistribution() {
        // Mock Data for Pie Chart
        ChartDataVO vo = new ChartDataVO();
        vo.setCategories(Arrays.asList("Critical", "Major", "Minor"));
        
        ChartDataVO.SeriesVO series = new ChartDataVO.SeriesVO();
        series.setName("Issue Levels");
        series.setType("pie");
        series.setData(Arrays.asList(
            new PieData("Critical", 5),
            new PieData("Major", 15),
            new PieData("Minor", 40)
        ));
        
        vo.setSeries(Arrays.asList(series));
        return vo;
    }

    @Override
    public ChartDataVO getTrendAnalysis() {
        // Mock Data for Line Chart (Last 7 days)
        ChartDataVO vo = new ChartDataVO();
        vo.setCategories(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        
        ChartDataVO.SeriesVO s1 = new ChartDataVO.SeriesVO();
        s1.setName("Critical Issues");
        s1.setType("line");
        s1.setData(Arrays.asList(1, 0, 2, 0, 1, 0, 0));
        
        ChartDataVO.SeriesVO s2 = new ChartDataVO.SeriesVO();
        s2.setName("Total Inspections");
        s2.setType("bar");
        s2.setData(Arrays.asList(10, 12, 10, 15, 14, 8, 5));
        
        vo.setSeries(Arrays.asList(s1, s2));
        return vo;
    }

    @Override
    public ChartDataVO getDeptRiskProfile() {
        // Mock Data for Radar Chart
        ChartDataVO vo = new ChartDataVO();
        // Indicators
        vo.setCategories(Arrays.asList("Hygiene", "Food Safety", "Equipment", "Process", "Personnel"));
        
        ChartDataVO.SeriesVO s1 = new ChartDataVO.SeriesVO();
        s1.setName("Canteen A");
        s1.setType("radar");
        s1.setData(Arrays.asList(80, 90, 70, 85, 95));
        
        vo.setSeries(Arrays.asList(s1));
        return vo;
    }
    
    // Helper class for Pie Data structure if needed, or just use Map
    static class PieData {
        public String name;
        public Object value;
        public PieData(String name, Object value) { this.name = name; this.value = value; }
    }
}
