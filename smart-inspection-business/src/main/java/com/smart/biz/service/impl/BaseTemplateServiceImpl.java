package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BaseTemplate;
import com.smart.biz.mapper.BaseTemplateMapper;
import com.smart.biz.service.IBaseTemplateService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.biz.domain.BaseTemplateItem;
import com.smart.biz.service.IBaseTemplateItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplate> implements IBaseTemplateService {

    @Autowired
    private IBaseTemplateItemService templateItemService;

    @Override
    @Transactional
    public boolean insertTemplate(BaseTemplate template) {
        boolean result = save(template);
        insertTemplateItem(template);
        return result;
    }

    @Override
    @Transactional
    public boolean updateTemplate(BaseTemplate template) {
        boolean result = updateById(template);
        templateItemService.remove(new LambdaQueryWrapper<BaseTemplateItem>().eq(BaseTemplateItem::getTemplateId, template.getTemplateId()));
        insertTemplateItem(template);
        return result;
    }

    @Override
    public List<Long> selectTemplateItemIds(Long templateId) {
        return templateItemService.list(new LambdaQueryWrapper<BaseTemplateItem>().eq(BaseTemplateItem::getTemplateId, templateId))
                .stream().map(BaseTemplateItem::getItemId).collect(Collectors.toList());
    }

    private void insertTemplateItem(BaseTemplate template) {
        if (template.getItemIds() != null && !template.getItemIds().isEmpty()) {
            List<BaseTemplateItem> list = new ArrayList<>();
            for (Long itemId : template.getItemIds()) {
                BaseTemplateItem ti = new BaseTemplateItem();
                ti.setTemplateId(template.getTemplateId());
                ti.setItemId(itemId);
                list.add(ti);
            }
            templateItemService.saveBatch(list);
        }
    }
}
