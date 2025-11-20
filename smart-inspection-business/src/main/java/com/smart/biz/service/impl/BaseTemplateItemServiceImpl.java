package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BaseTemplateItem;
import com.smart.biz.mapper.BaseTemplateItemMapper;
import com.smart.biz.service.IBaseTemplateItemService;
import org.springframework.stereotype.Service;

@Service
public class BaseTemplateItemServiceImpl extends ServiceImpl<BaseTemplateItemMapper, BaseTemplateItem> implements IBaseTemplateItemService {
}
