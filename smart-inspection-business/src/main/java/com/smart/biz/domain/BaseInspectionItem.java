package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_inspection_item")
public class BaseInspectionItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long itemId;

    private String itemName;
    private Long categoryId;
    private Long defaultLevelId;
    private String standardDesc;
    private String standardImg;
    private Integer isRequired;
}
