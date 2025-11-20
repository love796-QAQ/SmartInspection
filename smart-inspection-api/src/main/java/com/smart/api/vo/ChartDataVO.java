package com.smart.api.vo;

import lombok.Data;
import java.util.List;

@Data
public class ChartDataVO {
    private List<String> categories; // X-axis labels
    private List<SeriesVO> series;   // Data series
    
    @Data
    public static class SeriesVO {
        private String name;
        private String type; // line, bar, pie
        private List<Object> data;
    }
}
