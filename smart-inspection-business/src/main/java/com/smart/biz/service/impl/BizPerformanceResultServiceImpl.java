package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizPerformanceResult;
import com.smart.biz.mapper.BizPerformanceResultMapper;
import com.smart.biz.service.IBizPerformanceResultService;
import org.springframework.stereotype.Service;

@Service
public class BizPerformanceResultServiceImpl extends ServiceImpl<BizPerformanceResultMapper, BizPerformanceResult> implements IBizPerformanceResultService {
}
