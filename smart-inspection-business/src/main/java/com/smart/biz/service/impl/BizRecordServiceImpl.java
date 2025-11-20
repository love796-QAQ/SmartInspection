package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizRecord;
import com.smart.biz.mapper.BizRecordMapper;
import com.smart.biz.service.IBizRecordService;
import org.springframework.stereotype.Service;

@Service
public class BizRecordServiceImpl extends ServiceImpl<BizRecordMapper, BizRecord> implements IBizRecordService {
}
