package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BaseInspectionItem;
import com.smart.biz.mapper.BaseInspectionItemMapper;
import com.smart.biz.service.IBaseInspectionItemService;
import org.springframework.stereotype.Service;

@Service
public class BaseInspectionItemServiceImpl extends ServiceImpl<BaseInspectionItemMapper, BaseInspectionItem> implements IBaseInspectionItemService {
}
