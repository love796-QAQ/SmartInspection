package com.smart.biz.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_punish_record")
public class BizPunishRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long punishId;

    private Long suggestionId;
    private Long userId;
    private BigDecimal finalAmount;
    private Long confirmUserId;
    private LocalDateTime confirmTime;
}
