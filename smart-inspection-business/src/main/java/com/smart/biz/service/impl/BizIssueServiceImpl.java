package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizIssue;
import com.smart.biz.mapper.BizIssueMapper;
import com.smart.biz.service.IBizIssueService;
import org.springframework.stereotype.Service;

@Service
public class BizIssueServiceImpl extends ServiceImpl<BizIssueMapper, BizIssue> implements IBizIssueService {
}
