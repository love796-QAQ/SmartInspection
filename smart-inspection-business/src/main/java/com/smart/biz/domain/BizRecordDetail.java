package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("biz_record_detail")
public class BizRecordDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long detailId;

    private Long recordId;
    private Long taskItemId;
    
    /**
     * 1:Pass, 0:Fail
     */
    private Integer isPass;
    
    private Long actualLevelId;
    private String imageUrl;
    private String comment;
}
