package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("biz_task_item")
public class BizTaskItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long taskItemId;

    private Long taskId;
    private Long originalItemId;
    private String itemName;
    private Long categoryId;
    private Long levelId;
    private String standardDesc;
    private Integer sortOrder;
}
