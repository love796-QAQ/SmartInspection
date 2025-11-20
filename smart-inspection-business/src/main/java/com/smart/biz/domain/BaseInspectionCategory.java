package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("base_inspection_category")
public class BaseInspectionCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long categoryId;

    private String categoryName;
    private Integer sortOrder;
}
