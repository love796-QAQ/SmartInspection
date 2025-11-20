package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("base_template_item")
public class BaseTemplateItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long templateId;
    private Long itemId;
    private Integer sortOrder;
}
