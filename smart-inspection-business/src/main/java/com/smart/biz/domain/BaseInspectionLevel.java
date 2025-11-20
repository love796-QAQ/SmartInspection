package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("base_inspection_level")
public class BaseInspectionLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long levelId;

    private String levelName;
    private String levelCode;
    private String color;
    private Integer scoreWeight;
    
    /**
     * JSON: {"consecutive": 3, "amount": 50}
     */
    private String punishRuleJson;
    
    private Integer isEnabled;
}
