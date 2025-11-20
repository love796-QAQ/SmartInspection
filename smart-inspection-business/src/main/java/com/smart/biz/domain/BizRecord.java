package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("biz_record")
public class BizRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long recordId;

    private Long taskId;
    private Long inspectorId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
    
    private String gpsLocation;
    private String deviceInfo;
    private Integer isOfflineSubmission;
    
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private java.util.List<BizRecordDetail> details;
}
