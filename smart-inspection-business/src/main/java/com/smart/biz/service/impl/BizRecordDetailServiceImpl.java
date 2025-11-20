package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizRecordDetail;
import com.smart.biz.mapper.BizRecordDetailMapper;
import com.smart.biz.service.IBizRecordDetailService;
import org.springframework.stereotype.Service;

@Service
public class BizRecordDetailServiceImpl extends ServiceImpl<BizRecordDetailMapper, BizRecordDetail> implements IBizRecordDetailService {
}
