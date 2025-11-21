package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BaseInspectionCategory;
import com.smart.biz.mapper.BaseInspectionCategoryMapper;
import com.smart.biz.service.IBaseInspectionCategoryService;
import org.springframework.stereotype.Service;

@Service
public class BaseInspectionCategoryServiceImpl extends ServiceImpl<BaseInspectionCategoryMapper, BaseInspectionCategory> implements IBaseInspectionCategoryService {
}
