package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BaseTemplate;
import com.smart.biz.mapper.BaseTemplateMapper;
import com.smart.biz.service.IBaseTemplateService;
import org.springframework.stereotype.Service;

@Service
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplate> implements IBaseTemplateService {
}
