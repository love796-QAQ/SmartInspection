package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizPunishRecord;
import com.smart.biz.mapper.BizPunishRecordMapper;
import com.smart.biz.service.IBizPunishRecordService;
import org.springframework.stereotype.Service;

@Service
public class BizPunishRecordServiceImpl extends ServiceImpl<BizPunishRecordMapper, BizPunishRecord> implements IBizPunishRecordService {
}
